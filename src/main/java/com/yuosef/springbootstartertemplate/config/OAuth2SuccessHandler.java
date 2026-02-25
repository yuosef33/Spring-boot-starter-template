package com.yuosef.springbootstartertemplate.config;

import com.yuosef.springbootstartertemplate.repository.OAuthCodeDao;
import com.yuosef.springbootstartertemplate.models.OAuthCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuthCodeDao oAuthCodeRepository;

    // redirect after successful login
    @Value("${application.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        // delete any old code for this email
        oAuthCodeRepository.deleteByEmail(email);

        // generate a short-lived one-time used code expires in 60 seconds
        OAuthCode oAuthCode = OAuthCode.builder()
                .code(UUID.randomUUID().toString())
                .email(email)
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        oAuthCodeRepository.save(oAuthCode);

        // redirect with short-lived code not the actual token
        // code is useless after 60 seconds
        String targetUrl = redirectUri + "?code=" + oAuthCode.getCode();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);    }
}