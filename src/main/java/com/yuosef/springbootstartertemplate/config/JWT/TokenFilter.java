package com.yuosef.springbootstartertemplate.config.JWT;

import com.yuosef.springbootstartertemplate.models.User;
import com.yuosef.springbootstartertemplate.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.SystemException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenHandler tokenHandler;
    private final UserService userService;

    private static final List<String> EXACT_PUBLIC_PATHS = List.of(
            "/auth/signup",
            "/auth/Login",
            "/auth/createUser",
            "/auth/refresh-token",
            "/auth/oauth2/callback",
            "/auth/exchange-code"
    );

    private static final List<String> PREFIX_PUBLIC_PATHS = List.of(
            "/swagger-ui",
            "/v3/api-docs",
            "/h2-console",
            "/actuator",
            "/oauth2",        // ← add this
            "/login/oauth2"
    );

    public TokenFilter(TokenHandler tokenHandler, UserService userService) {
        this.tokenHandler = tokenHandler;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return EXACT_PUBLIC_PATHS.contains(path) ||
                PREFIX_PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");
        if ( token ==null||!token.startsWith("Bearer")){
            response.reset();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        token=token.substring(7);
        String email = tokenHandler.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {

                User user = userService.getUserByEmail(email);
                if (tokenHandler.isTokenValid(token, user)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);

    }


}
