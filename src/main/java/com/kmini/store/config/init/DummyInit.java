package com.kmini.store.config.init;

import com.kmini.store.aop.CategoryHolder;
import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.Category;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplyReqDto;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.CommentService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.kmini.store.domain.type.CategoryType.*;

@Component
@Profile({"default", "dev", "local"})
@Slf4j
@RequiredArgsConstructor
public class DummyInit implements ApplicationRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemBoardService itemBoardService;
    private final CommentService commentService;
    private final CategoryHolder categoryHolder;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("DevInit 실행...");
        User user = new User("kmini", "1234", "test@gmail.com", UserRole.USER, UserStatus.SIGNUP, null);
        userService.save(user);
        User admin = new User("admin", passwordEncoder.encode("admin"), "admin@gmail.com", UserRole.ADMIN, UserStatus.SIGNUP, null);
        userRepository.save(admin);

        log.info("카테고리 넣기! ..");
        Category trade = new Category(TRADE);
        Category community = new Category(COMMUNITY);
        categoryRepository.save(trade);
        categoryRepository.save(community);

        log.info("소카테고리 넣기! ..");
        categoryRepository.save(new Category(ELECTRONICS, trade));
        categoryRepository.save(new Category(FOODS, trade));
        categoryRepository.save(new Category(FREE, community));
        categoryRepository.save(new Category(QNA, community));

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
            ItemBoardFormSaveDto saveFormDto =
                    new ItemBoardFormSaveDto("trade", "electronics", "title" + i, "content" + i, null, "item" + i);
            itemBoardService.save(saveFormDto, principal);
        }

        log.info("상위 댓글 넣기 !!..");
        commentService.saveComment(new BoardCommentReqDto(156L, "상위 댓글댓글1111"), user);
        commentService.saveComment(new BoardCommentReqDto(156L, "상위 댓글댓글2222"), user);

        log.info("대댓글 넣기!!");
        commentService.saveReply(new BoardReplyReqDto(156L, 1L, "댓글1의 대댓글1"), user);
        commentService.saveReply(new BoardReplyReqDto(156L, 1L, "댓글1의 대댓글2"), user);
        commentService.saveReply(new BoardReplyReqDto(156L, 2L, "댓글2의 대댓글1"), user);
        commentService.saveReply(new BoardReplyReqDto(156L, 2L, "댓글2의 대댓글2"), user);

//        for (int i = 157; i < 300; i++) {
//            FormSaveDto saveFormDto =
//                    new FormSaveDto(COMMUNITY.getNameWithLowerCase(), FREE.getNameWithLowerCase(),
//                            "title" + i, "content" + i, null, "item" + i);
//            boardService.save(saveFormDto,principal);
//        }
    }
}
