package com.kmini.store.domain;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.converter.GenderAttributeConverter;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="Users")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@ToString
public class User extends BaseTime {

    public User() {
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    private String username;

    private String password;

    // 변경 불가
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String thumbnail;

    @Convert(converter = GenderAttributeConverter.class)
    private Gender gender;

    private LocalDate birthdate;

    @Transient
    private MultipartFile file;

    public User(String username, String password, String email,
                UserRole role, UserStatus userStatus, String thumbnail,
                Gender gender, LocalDate birthdate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.userStatus = userStatus;
        this.thumbnail = thumbnail;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public static User getSecurityContextUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("로그인된 상태가 아닙니다.");
        }
        return ((AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    public static void updateSecurityContext(User user) {
        AccountContext accountContext = new AccountContext(user);
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(accountContext, null, accountContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
