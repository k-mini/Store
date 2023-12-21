package com.kmini.store.config;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        User user = new User(customUser.username(), customUser.password(), customUser.email(), customUser.role(), UserStatus.SIGNUP, null);
        User savedUser = userRepository.save(user);
        AccountContext accountContext = new AccountContext(savedUser);

//        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserByUsername(customUser.email());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(accountContext, null, accountContext.getAuthorities());
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    public SecurityContext createSecurityContext(String username, String password, String email) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        User user = new User(username, password, email, UserRole.USER, UserStatus.SIGNUP, null);
        User savedUser = userRepository.save(user);
        AccountContext accountContext = new AccountContext(savedUser);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(accountContext, null, accountContext.getAuthorities());
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
