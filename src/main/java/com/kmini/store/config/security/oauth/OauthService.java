package com.kmini.store.config.security.oauth;

import com.kmini.store.config.security.jwt.JwtTokenUtil;
import com.kmini.store.config.security.oauth.OAuthAttributes.OAuth2UserProfile;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.UserRole.USER;
import static com.kmini.store.domain.type.UserStatus.SIGNUP;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthService implements OAuth2UserService {

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 사용자 정보 가져오기
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                                                .getProviderDetails()
                                                .getUserInfoEndpoint()
                                                .getUserNameAttributeName();
        // 사용자 이메일 추출
        OAuth2UserProfile oAuth2UserProfile = OAuthAttributes.extractUser(registrationId, oAuth2User.getAttributes());
        String email = oAuth2UserProfile.getEmail();
        String nickname = oAuth2UserProfile.getUsername();

        User user;
        try {
            user = userService.selectUserByEmail(email);
            log.info("기존 이메일이 존재합니다. 해당 이메일로 로그인 진행..");
        } catch (UsernameNotFoundException e) {
            log.info("User가 존재하지 않습니다. 회원가입을 진행합니다.");
            user = new User();
            user.setEmail(email);
            user.setUsername(nickname);
            user.setRole(USER);
            user.setUserStatus(SIGNUP);
            user.setGender(MAN);
            user.setBirthdate(LocalDate.now());
            // 비밀번호가 있게 DB를 설계했으므로 임시 비밀번호 실정.. 향후 수정
            user.setPassword("kmini1234");
            userService.saveUser(user);
        }

        return new CustomOAuth2User(oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),userNameAttributeName, user);
    }
}
