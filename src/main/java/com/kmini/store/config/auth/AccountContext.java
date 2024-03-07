package com.kmini.store.config.auth;

import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AccountContext extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    public User user;

    public AccountContext(User user) {
        super(user.getUsername(), user.getPassword(), getAuthority(user.getRole()) );
        this.user = user;
    }

    public static Collection<? extends GrantedAuthority> getAuthority(UserRole role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
