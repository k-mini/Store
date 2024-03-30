package com.kmini.store.global.config.security.oauth;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    KAKAO("kakao", (attributes) -> {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        OAuth2UserProfile user = new OAuth2UserProfile();
        user.setEmail((String) kakaoAccount.get("email"));
        user.setUsername((String) kakaoProfile.get("nickname"));
        return user;
    }),
    GOOGLE("google", (attributes) -> {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        OAuth2UserProfile user = new OAuth2UserProfile();
        user.setEmail(email);
        user.setUsername(name);
        return user;
    }),
    GITHUB("github", (attributes) -> {
        // 임시로 이메일 지정 추후 수정 필요..
        String email = String.format("%s@github.com", attributes.get("login").toString());
        String name = (String) attributes.get("name");
        OAuth2UserProfile user = new OAuth2UserProfile();
        user.setEmail(email);
        user.setUsername(name);
        return user;
    }),
    ;

    private final String registrationId;
    private final Function<Map<String, Object>, OAuth2UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, OAuth2UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public static OAuth2UserProfile extractUser(String registrationInd, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> provider.getRegistrationId().equals(registrationInd))
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .of.apply(attributes);
    }

    @Getter @Setter
    public static class OAuth2UserProfile {
        private String email;
        private String username;
    }

}
