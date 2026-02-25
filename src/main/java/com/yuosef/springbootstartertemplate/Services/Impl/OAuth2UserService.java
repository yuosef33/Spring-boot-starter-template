package com.yuosef.springbootstartertemplate.Services.Impl;

import com.yuosef.springbootstartertemplate.Daos.AuthorityDao;
import com.yuosef.springbootstartertemplate.Daos.UserDao;
import com.yuosef.springbootstartertemplate.Models.Authority;
import com.yuosef.springbootstartertemplate.Models.Dtos.AuthProvider;
import com.yuosef.springbootstartertemplate.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserDao userRepository;
    private final AuthorityDao roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // load user info from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        userRepository.findUserByEmail(email).orElseGet(() -> {
            Authority userRole = roleRepository.findByUserRole("USER")
                    .orElseThrow(() -> new IllegalStateException("Role USER not found"));

            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .pwd(null)          ///    ---------------------  is setting password = null a good solution ?
                    .authProvider(AuthProvider.GOOGLE)
                    .authorities(List.of(userRole))
                    .build();

            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}