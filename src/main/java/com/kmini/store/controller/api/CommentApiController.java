package com.kmini.store.controller.api;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplyRespDto;
import com.kmini.store.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(
            @RequestBody BoardCommentSaveDto boardCommentSaveDto,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        log.info("boardCommentReqDto = {}", boardCommentSaveDto);
        BoardCommentRespDto result = commentService.saveComment(boardCommentSaveDto, principalDetail.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody BoardCommentUpdateDto boardCommentUpdateDto) {
        log.debug("boardCommentUpdateDto = {}", boardCommentUpdateDto);
        commentService.update(boardCommentUpdateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1,"성공", null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId, @AuthenticationPrincipal PrincipalDetail principalDetail
    ) {
        log.info("commentId = {}", commentId);
        User user = principalDetail.getUser();
        int result = commentService.delete(commentId, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    @PostMapping("{commentId}/reply")
    public ResponseEntity<?> saveReply(
            @RequestBody BoardReplySaveDto boardReplySaveDto,
            @AuthenticationPrincipal PrincipalDetail principalDetail
    ) {
        log.info("boardReplyReqDto = {} ", boardReplySaveDto);
        BoardReplyRespDto result = commentService.saveReply(boardReplySaveDto, principalDetail.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
