package com.kmini.store.domain.user.repository;

import com.kmini.store.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String username);
}
