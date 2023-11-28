package com.kmini.store.config.init;

import com.kmini.store.aop.CategoryHolder;
import com.kmini.store.config.auth.AccountContext;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.kmini.store.domain.type.CategoryType.*;

@Component
@Profile({"default", "dev", "local"})
@Slf4j
@RequiredArgsConstructor
public class StoreInitializer implements ApplicationRunner {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemBoardServiceImpl itemBoardService;
    private final CommunityBoardServiceImpl communityBoardService;
    private final CommentServiceImpl commentService;
    private final TradeServiceImpl tradeService;
    private final CategoryHolder categoryHolder;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("StoreInitializer 실행...");

        User user = createUser("kmini", "1111", "test@gmail.com");
        createUser("kmini2", "1111", "test2@gmail.com");
        createAdmin("admin", "admin", "admin@gmail.com");

        categorySetUp();

//        log.info("인증 객체 저장");
        AccountContext accountContext = new AccountContext(user);
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.authenticated(new AccountContext(user), null, List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
        SecurityContextHolder.getContext().setAuthentication(token);

        log.info("거래 게시판 샘플 게시물 넣기! ..");

        for (int i = 1; i < 157; i++) {
            ItemBoardFormSaveDto saveFormDto =
                    new ItemBoardFormSaveDto( "electronics", "title" + i, "content" + i, null, "item" + i);
            itemBoardService.save(saveFormDto);
        }
        log.debug("커뮤니티 게사판 샘플 데이터 넣기! ...");
        for (int i = 1; i < 93; i++) {
            CommunityBoardFormSaveDto saveFormDto =
                    new CommunityBoardFormSaveDto( "free", "comtitle" + i, "comcontent" + i, null);
            communityBoardService.save(saveFormDto);
        }

        log.info("상위 댓글 넣기 !!..");
        commentService.saveComment(new BoardCommentSaveDto(156L, "상위 댓글댓글1111"), user);
        commentService.saveComment(new BoardCommentSaveDto(156L, "상위 댓글댓글2222"), user);

        log.info("대댓글 넣기!!");
        commentService.saveReply(new BoardReplySaveDto(156L, 1L, "댓글1의 대댓글1"), user);
        commentService.saveReply(new BoardReplySaveDto(156L, 1L, "댓글1의 대댓글2"), user);
        commentService.saveReply(new BoardReplySaveDto(156L, 2L, "댓글2의 대댓글1"), user);
        commentService.saveReply(new BoardReplySaveDto(156L, 2L, "댓글2의 대댓글2"), user);

        log.info("거래 넣기");
        tradeService.registerTrade(1L);
        tradeService.registerTrade(2L);
        tradeService.registerTrade(3L);
        tradeService.registerTrade(4L);

//        for (int i = 157; i < 300; i++) {
//            FormSaveDto saveFormDto =
//                    new FormSaveDto(COMMUNITY.getNameWithLowerCase(), FREE.getNameWithLowerCase(),
//                            "title" + i, "content" + i, null, "item" + i);
//            boardService.save(saveFormDto,principal);
//        }
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
