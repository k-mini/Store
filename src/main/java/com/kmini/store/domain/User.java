package com.kmini.store.domain;

import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="Users")
@Getter @Setter
@AllArgsConstructor
@Builder
@ToString
public class User {

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

    public User(String username, String password, String email, UserRole role, UserStatus userStatus, String thumbnail) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.userStatus = userStatus;
        this.thumbnail = thumbnail;
    }
}
