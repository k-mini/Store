package com.kmini.store.config.security.oauth;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private AccountContext accountContext;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
        super(authorities, attributes, nameAttributeKey);
        this.accountContext = new AccountContext(user);
    }

    public AccountContext getAccountContext(){
        return this.accountContext;
    }
}
