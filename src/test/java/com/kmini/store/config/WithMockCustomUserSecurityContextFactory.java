package com.kmini.store.config;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.auth.CustomUserDetailsService;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        User user = new User(customUser.username(), customUser.password(), customUser.email(), UserRole.USER, UserStatus.SIGNUP, null);
        userRepository.save(user);
        AccountContext accountContext = new AccountContext(user);

//        AccountContext accountContext = (AccountContext) customUserDetailsService.loadUserByUsername(customUser.email());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(accountContext, null, accountContext.getAuthorities());
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
