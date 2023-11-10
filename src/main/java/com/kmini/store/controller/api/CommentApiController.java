package com.kmini.store.controller.api;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplyReqDto;
import com.kmini.store.dto.response.CommentDto;
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
            @RequestBody BoardCommentReqDto boardCommentReqDto,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        log.info("boardCommentReqDto = {}", boardCommentReqDto);
        BoardCommentRespDto result = commentService.saveComment(boardCommentReqDto, principalDetail.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1,"성공", result));
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
            @RequestBody BoardReplyReqDto boardReplyReqDto,
            @AuthenticationPrincipal PrincipalDetail principalDetail
    ) {
        log.info("boardReplyReqDto = {} ", boardReplyReqDto);
        BoardReplyRespDto result = commentService.saveReply(boardReplyReqDto, principalDetail.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
