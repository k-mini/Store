package com.kmini.store.config.init;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.BoardType;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.ItemBoardUploadDto;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.service.ItemBoardService;
import com.kmini.store.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import static com.kmini.store.domain.type.BoardType.*;

@Component
@Profile({"default","dev","local"})
@Slf4j
@RequiredArgsConstructor
public class DummyInit implements ApplicationRunner {

    private final UserService userService;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ItemBoardService itemBoardService;
    private final CategoryHolder categoryHolder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DevInit 실행...");
        User user = new User("kmini", "1234", "test@gmail.com", UserRole.USER, UserStatus.SIGNUP, null);
        userService.save(user);

        log.info("카테고리 넣기! ..");
        BoardCategory community = new BoardCategory(COMMUNITY);
        BoardCategory trade = new BoardCategory(TRADE);
        boardCategoryRepository.save(community);
        boardCategoryRepository.save(trade);

        log.info("소카테고리 넣기! ..");
        boardCategoryRepository.save(new BoardCategory(ELECTRONICS,trade));
        boardCategoryRepository.save(new BoardCategory(FOODS,trade));
        boardCategoryRepository.save(new BoardCategory(FREE,community));
        boardCategoryRepository.save(new BoardCategory(QNA,community));

        log.info("서버에 카테고리 정보 저장..");
        categoryHolder.getMap().add(TRADE, ELECTRONICS);
        categoryHolder.getMap().add(TRADE, FOODS);
        categoryHolder.getMap().add(COMMUNITY, FREE);
        categoryHolder.getMap().add(COMMUNITY, QNA);

        log.info("인증 정보 넣기");
        PrincipalDetail principal = new PrincipalDetail(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("샘플 게시물 넣기! ..");

        for (int i = 1; i < 157; i++) {
            ItemBoardUploadDto itemBoardUploadDto =
                    new ItemBoardUploadDto(2L, "title" + i, "content" + i, null, "item" + i);
            itemBoardService.upload(itemBoardUploadDto,principal);
        }
    }
}
