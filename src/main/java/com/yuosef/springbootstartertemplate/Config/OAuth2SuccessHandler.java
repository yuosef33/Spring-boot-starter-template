package com.yuosef.springbootstartertemplate.Config;

import com.yuosef.springbootstartertemplate.Config.JWT.TokenHandler;
import com.yuosef.springbootstartertemplate.Daos.UserDao;
import com.yuosef.springbootstartertemplate.Models.User;
import com.yuosef.springbootstartertemplate.Services.Impl.RefreshTokenServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenHandler jwtService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final UserDao userRepository;

    // redirect after successful login
    @Value("${application.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // load user from DB
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found after OAuth2 login"));

        // generate tokens
        String accessToken  = jwtService.createToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        // redirect to frontend with tokens in URL
        // frontend reads them from URL and saves them
        String targetUrl = redirectUri +
                "?token=" + accessToken +
                "&refreshToken=" + refreshToken;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}