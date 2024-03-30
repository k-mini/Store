package com.kmini.store.global.init;

import com.kmini.store.domain.board.community.service.CommunityBoardService;
import com.kmini.store.domain.board.item.service.ItemBoardService;
import com.kmini.store.domain.board.category.service.CategoryService;
import com.kmini.store.domain.comment.service.CommentService;
import com.kmini.store.domain.board.trade.service.TradeService;
import com.kmini.store.domain.user.service.UserService;
import com.kmini.store.global.interceptor.CategoryInterceptor;
import com.kmini.store.global.config.security.auth.AccountContext;
import com.kmini.store.domain.entity.Comment;
import com.kmini.store.domain.entity.CommunityBoard;
import com.kmini.store.domain.entity.ItemBoard;
import com.kmini.store.domain.entity.User;
import com.kmini.store.global.constants.UserRole;
import com.kmini.store.global.constants.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

import static com.kmini.store.global.constants.Gender.MAN;
import static com.kmini.store.global.constants.Gender.WOMAN;

//@Component
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
            String title = "title" + i;
            String content = "content" + i;
            String itemName = "item" + i;
            ItemBoard itemBoard = ItemBoard.builder()
                    .title(title)
                    .content(content)
                    .itemName(itemName)
                    .subCategoryName("electronics")
                    .build();
            itemBoardService.saveBoard(itemBoard);
        }
        log.debug("커뮤니티 게사판 샘플 데이터 넣기! ...");
        for (int i = 1; i < 156L; i++) {
            String subCategoryName = "free";
            String title = "comtitle" + i;
            String content = "comcontent" + i;
            CommunityBoard newCommunityBoard = CommunityBoard.builder()
                    .subCategoryName(subCategoryName)
                    .title(title)
                    .content(content)
                    .build();
            communityBoardService.save(newCommunityBoard);
        }

        log.info("상위 댓글 넣기 !!..");
        Comment comment = new Comment(user, null, null, "상위 댓글댓글1111");
        Comment comment2 = new Comment(user, null, null, "상위 댓글댓글2222");
        commentService.saveComment(156L, null, comment);
        commentService.saveComment(156L, null, comment2);

        log.info("대댓글 넣기!!");
        Comment subComment1 = new Comment(user, null, null, "댓글1의 대댓글1");
        Comment subComment2 = new Comment(user, null, null, "댓글1의 대댓글2");
        Comment subComment3 = new Comment(user, null, null, "댓글2의 대댓글1");
        Comment subComment4 = new Comment(user, null, null, "댓글2의 대댓글2");
        commentService.saveComment(156L, 1L, subComment1);
        commentService.saveComment(156L, 1L, subComment2);
        commentService.saveComment(156L, 2L, subComment3);
        commentService.saveComment(156L, 2L, subComment4);

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
        return userService.saveUser(new User(username, password, email,
                UserRole.ADMIN, UserStatus.SIGNUP, null, MAN, LocalDate.of(1995,5,31)));
    }

    private User createUser(String username, String password, String email) {
        return userService.saveUser(new User(username, password, email,
                UserRole.USER, UserStatus.SIGNUP, null, WOMAN, LocalDate.of(1992,3,05)));
    }
}
