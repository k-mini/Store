package com.kmini.store.config.init;

import com.kmini.store.aop.CategoryInterceptor;
import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.BoardDto.CommunityBoardFormSaveDto;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveReqDto;
import com.kmini.store.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"default", "dev", "local"})
@Slf4j
@RequiredArgsConstructor
public class StoreInitializer implements ApplicationRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ItemBoardService itemBoardService;
    private final CommunityBoardService communityBoardService;
    private final CommentService commentService;
    private final TradeService tradeService;
    private final CategoryInterceptor categoryInterceptor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("StoreInitializer 실행...");

        User user = createUser("kmini", "1111", "test@gmail.com");
        createUser("kmini2", "1111", "test2@gmail.com");
        createAdmin("admin", "admin", "admin@gmail.com");

        categoryDataSetUp();

        log.info("인증 객체 저장");
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.authenticated(new AccountContext(user), null, List.of(new SimpleGrantedAuthority(UserRole.USER.name())));
        SecurityContextHolder.getContext().setAuthentication(token);

//        long randomCount = Double.valueOf(Math.random() * 100).longValue();

        log.info("거래 게시판 샘플 게시물 넣기! ..");
        for (int i = 1; i < 156L ; i++) {
            ItemBoardFormSaveDto saveFormDto =
                    new ItemBoardFormSaveDto( "electronics", "title" + i, "content" + i, null, "item" + i);
            itemBoardService.save(saveFormDto);
        }
        log.debug("커뮤니티 게사판 샘플 데이터 넣기! ...");
        for (int i = 1; i < 156L; i++) {
            CommunityBoardFormSaveDto saveFormDto =
                    new CommunityBoardFormSaveDto( "free", "comtitle" + i, "comcontent" + i, null);
            communityBoardService.save(saveFormDto);
        }

        log.info("상위 댓글 넣기 !!..");
        commentService.saveComment(new BoardCommentSaveReqDto(156L, "상위 댓글댓글1111"));
        commentService.saveComment(new BoardCommentSaveReqDto(156L, "상위 댓글댓글2222"));

        log.info("대댓글 넣기!!");
        commentService.saveReplyComment(new BoardReplySaveReqDto(156L, 1L, "댓글1의 대댓글1"));
        commentService.saveReplyComment(new BoardReplySaveReqDto(156L, 1L, "댓글1의 대댓글2"));
        commentService.saveReplyComment(new BoardReplySaveReqDto(156L, 2L, "댓글2의 대댓글1"));
        commentService.saveReplyComment(new BoardReplySaveReqDto(156L, 2L, "댓글2의 대댓글2"));

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

    private void categoryDataSetUp() {
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
        return userService.saveUser(new User(username, password, email, UserRole.ADMIN, UserStatus.SIGNUP, null));
    }

    private User createUser(String username, String password, String email) {
        return userService.saveUser(new User(username, password, email, UserRole.USER, UserStatus.SIGNUP, null));
    }
}
