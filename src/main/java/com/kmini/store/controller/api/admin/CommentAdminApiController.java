package com.kmini.store.controller.api.admin;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.response.admin.AdminBoardResponseDto;
import com.kmini.store.dto.response.admin.AdminCommentResponseDto;
import com.kmini.store.dto.response.admin.AdminCommentResponseDto.AdminCommentDto;
import com.kmini.store.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 댓글, 대댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.debug("commentId = {}", commentId);
        int result = commentService.deleteComment(commentId);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", result));
    }
}
