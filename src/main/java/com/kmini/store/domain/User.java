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
public class User {
    public User() {
    }
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="USER_ID")
    private Long id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String thumbnail;

}
