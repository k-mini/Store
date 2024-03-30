package com.kmini.store.domain.admin.comment;

import com.kmini.store.domain.comment.service.CommentService;
import com.kmini.store.domain.common.dto.CommonRespDto;
import com.kmini.store.domain.admin.board.dto.AdminBoardResponseDto;
import com.kmini.store.domain.admin.comment.dto.AdminCommentResponseDto.AdminCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class CommentAdminApiController {

    private final CommentService commentService;

    // 전체 게시물 조회
    @GetMapping("/comments")
    public ResponseEntity<?> viewBoardList(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) Optional<Integer> draw) {

        Page<AdminCommentDto> page = commentService.selectAllComments(pageable);

        AdminBoardResponseDto<AdminCommentDto> result = new AdminBoardResponseDto<>(draw.orElse(0), page);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", result));
    }

    // 댓글 여러 개 삭제
    @DeleteMapping("/comments")
    public ResponseEntity<?> deleteComments(@RequestParam("commentIds") List<Long> commentIds) {
        log.debug("commentIds = {}", commentIds);
        int result = commentService.deleteComments(commentIds);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", result));
    }

    // 댓글, 대댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.debug("commentId = {}", commentId);
        int result = commentService.deleteComment(commentId);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", result));
    }
}
