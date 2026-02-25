package com.yuosef.springbootstartertemplate.services.Impl;

import com.yuosef.springbootstartertemplate.repository.AuthorityDao;
import com.yuosef.springbootstartertemplate.repository.UserDao;
import com.yuosef.springbootstartertemplate.models.Authority;
import com.yuosef.springbootstartertemplate.models.AuthProvider;
import com.yuosef.springbootstartertemplate.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserDao userRepository;
    private final AuthorityDao roleRepository;
    private final PasswordEncoder passwordEncoder;

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
                    .pwd(passwordEncoder.encode(UUID.randomUUID().toString()))          /// saving any random hashed value
                    .authProvider(AuthProvider.GOOGLE)
                    .authorities(List.of(userRole))
                    .build();

            return userRepository.save(newUser);
        });

        return oAuth2User;
    }
}