package com.yuosef.springbootstartertemplate.Config.Bucket4J;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    // one bucket per IP address
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Auth endpoints get stricter limiting
    private static final int AUTH_LIMIT = 5;
    private static final int AUTH_REFILL_MINUTES = 1;

    // Global limit for all other endpoints
    private static final int GLOBAL_LIMIT = 100;
    private static final int GLOBAL_REFILL_MINUTES = 1;

    @Override
    protected void doFilterInternal(
             HttpServletRequest request,
             HttpServletResponse response,
             FilterChain filterChain
    ) throws ServletException, IOException {


        String ip = getClientIP(request);
        String path = request.getServletPath();

        String bucketKey = ip + ":" + (isAuthPath(path) ? "auth" : "global");

        Bucket bucket = buckets.computeIfAbsent(bucketKey, k -> createBucket(path));

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for IP: {} on path: {}", ip, path);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("""
                {
                    "success": false,
                    "message": "Too many requests, please slow down and try again later.",
                    "data": null
                }
                """);
        }
    }

    // create bucket based on the endpoint type
    private Bucket createBucket(String path) {
        if (isAuthPath(path)) {
            //  limit for auth endpoints
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(
                            AUTH_LIMIT,
                            Refill.greedy(AUTH_LIMIT, Duration.ofMinutes(AUTH_REFILL_MINUTES))
                    ))
                    .build();
        }

        // global limit for everything else
        return Bucket.builder()
                .addLimit(Bandwidth.classic(
                        GLOBAL_LIMIT,
                        Refill.greedy(GLOBAL_LIMIT, Duration.ofMinutes(GLOBAL_REFILL_MINUTES))
                ))
                .build();
    }

    private boolean isAuthPath(String path) {
        return path.startsWith("/auth/");
    }

    // get real IP even behind a proxy or load balancer
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}