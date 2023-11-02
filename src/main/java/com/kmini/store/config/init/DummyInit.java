package com.kmini.store.config.init;

import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.BoardType;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"default","dev","local"})
@Slf4j
@RequiredArgsConstructor
public class DummyInit implements ApplicationRunner {

    private final UserService userService;
    private final BoardCategoryRepository boardCategoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DevInit 실행...");
        User user = new User("kmini", "1234", "test@gmail.com", UserRole.USER, UserStatus.SIGNUP, null);
        userService.save(user);

        log.info("카테고리 넣기! ..");
        boardCategoryRepository.save(new BoardCategory(BoardType.COMMUNITY));
        boardCategoryRepository.save(new BoardCategory(BoardType.TRADE));
    }
}
