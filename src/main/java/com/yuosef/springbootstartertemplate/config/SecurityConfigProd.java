package com.yuosef.springbootstartertemplate.config;

import com.yuosef.springbootstartertemplate.config.Bucket4J.RateLimitFilter;
import com.yuosef.springbootstartertemplate.config.JWT.TokenFilter;
import com.yuosef.springbootstartertemplate.repository.UserDao;
import com.yuosef.springbootstartertemplate.services.Impl.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;



// OAuth2 is configured in dev profile only.
// For prod configure OAuth2 with your actual Google credentials
@Configuration
@RequiredArgsConstructor
@Profile("prod")
public class SecurityConfigProd {

    private final UserDao userRepository;

    // configurable from env variable
    @Value("${application.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           TokenFilter tokenFilter,
                                           RateLimitFilter rateLimitFilter,
                                           OAuth2SuccessHandler oAuth2SuccessHandler,
                                           OAuth2UserService oAuth2UserService) throws Exception {

        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    // restricted to your actual frontend URL in prod
                    corsConfiguration.setAllowedOriginPatterns(List.of(allowedOrigins));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setMaxAge(3600L);
                    return corsConfiguration;
                }))

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()
                        // no H2 console in prod
                        // no swagger in prod
                        .requestMatchers(HttpMethod.POST, "/business/logout").authenticated()
                        .requestMatchers(HttpMethod.GET, "/business/hello").hasRole("USER")
                        .anyRequest().authenticated()
                )

                // stricter headers for prod
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                        )
                        .contentSecurityPolicy(csp ->
                                csp.policyDirectives("default-src 'self'")
                        )
                )

                .csrf(csrf -> csrf.disable())

                .authenticationProvider(authenticationProvider())

                // OAuth2 Google login — same as dev
                // make sure GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET
                // and OAUTH2_REDIRECT_URI are set in prod env variables
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                );

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(rateLimitFilter, TokenFilter.class);

        return http.build();
    }

    // Loads the user from database by email Spring Security calls this automatically
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    // Validates credentials during login (email and password)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
