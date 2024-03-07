package com.kmini.store.config.security.oauth;

import com.kmini.store.domain.User;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    KAKAO("kakao", (attributes) -> {

        Map<String,Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        User user = new User();
        user.setEmail((String) kakaoAccount.get("email"));
        user.setUsername((String) kakaoProfile.get("nickname"));
        return user;
    });
    private final String registrationId;
    private final Function<Map<String,Object>, User> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, User> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public static User extractUser(String registrationInd, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> provider.getRegistrationId().equals(registrationInd))
                .findFirst()
                .orElseThrow(IllegalAccessError::new)
                .of.apply(attributes);
    }
}
