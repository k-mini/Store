package com.kmini.store.config;

import com.kmini.store.aop.CategoryInterceptor;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.Gender;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.Gender.WOMAN;

@Component
@Profile({"test"})
@Slf4j
@RequiredArgsConstructor
public class StoreTestInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryService categoryService;
    private final CategoryInterceptor categoryInterceptor;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("StoreTestInitializer 실행...");

        User user = createUser("kmini", "1111", "test@gmail.com");
        createUser("kmini2", "1111", "test2@gmail.com");
        createAdmin("admin", "admin", "admin@gmail.com");

        categorySetUp();

//        log.info("인증 객체 저장");
//        AccountContext accountContext = new AccountContext(user);
//        UsernamePasswordAuthenticationToken token =
//                UsernamePasswordAuthenticationToken.authenticated(new AccountContext(user), null, List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
//        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private void categorySetUp() {
        log.info("카테고리 넣기! ..");
        categoryService.saveCategory("TRADE", "거래",  null);
        categoryService.saveCategory("COMMUNITY", "커뮤니티",null);

        log.info("소카테고리 넣기! ..");
        categoryService.saveCategory("ELECTRONICS", "전자", "TRADE");
        categoryService.saveCategory("CLOTHES", "의류","TRADE");
        categoryService.saveCategory("FREE", "자유","COMMUNITY");
        categoryService.saveCategory("QNA", "문의", "COMMUNITY");

        categoryInterceptor.reload();
    }

    private User createAdmin(String username, String password, String email) {
        return userRepository.save(new User(username, passwordEncoder.encode(password), email, UserRole.ADMIN, UserStatus.SIGNUP, null
                , MAN, LocalDate.of(2001,6,24)));
    }

    private User createUser(String username, String password, String email) {
        return userRepository.save(new User(username, passwordEncoder.encode(password), email, UserRole.USER,
                UserStatus.SIGNUP, null, WOMAN, LocalDate.of(1999,11,13)));
    }
}
