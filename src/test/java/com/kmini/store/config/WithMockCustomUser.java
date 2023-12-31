package com.kmini.store.config;

import com.kmini.store.domain.type.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String email() default "kmini@gmail.com";

    String username() default "kmini";

    String password() default "1111";

    UserRole role() default UserRole.USER;
}
