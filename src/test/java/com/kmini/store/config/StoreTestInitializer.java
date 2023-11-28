package com.kmini.store.config;

import com.kmini.store.aop.CategoryHolder;
import com.kmini.store.domain.Category;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.BoardDto.CommunityBoardFormSaveDto;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveDto;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.kmini.store.domain.type.CategoryType.*;

@Component
@Profile({"test"})
@Slf4j
@RequiredArgsConstructor
public class StoreTestInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryHolder categoryHolder;
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
        Category trade = createCategory(TRADE, null);
        Category community = createCategory(COMMUNITY, null);

        log.info("소카테고리 넣기! ..");
        createCategory(ELECTRONICS, trade);
        createCategory(FOODS, trade);
        createCategory(FREE, community);
        createCategory(QNA, community);

        log.info("서버에 카테고리 정보 저장..");
        categoryHolder.getMap().add(TRADE, ELECTRONICS);
        categoryHolder.getMap().add(TRADE, FOODS);
        categoryHolder.getMap().add(COMMUNITY, FREE);
        categoryHolder.getMap().add(COMMUNITY, QNA);
    }

    private Category createCategory(CategoryType type, Category parent) {
        return categoryRepository.save(new Category(type, parent));
    }

    private User createAdmin(String username, String password, String email) {
        return userRepository.save(new User(username, passwordEncoder.encode(password), email, UserRole.ADMIN, UserStatus.SIGNUP, null));
    }

    private User createUser(String username, String password, String email) {
        return userRepository.save(new User(username, passwordEncoder.encode(password), email, UserRole.USER, UserStatus.SIGNUP, null));
    }
}
