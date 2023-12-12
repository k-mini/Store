package com.kmini.store.controller.api;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentUpdateRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplySaveRespDto;
import com.kmini.store.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;

    // 댓글 저장
    @PostMapping
    public ResponseEntity<?> saveComment(
            @RequestBody BoardCommentSaveReqDto boardCommentSaveReqDto) {
        log.debug("boardCommentReqDto = {}", boardCommentSaveReqDto);
        BoardCommentUpdateRespDto result = commentService.saveComment(boardCommentSaveReqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 댓글,대댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody BoardCommentUpdateReqDto boardCommentUpdateReqDto) {
        log.debug("boardCommentUpdateReqDto = {}", boardCommentUpdateReqDto);
        BoardCommentUpdateRespDto result = commentService.updateComment(boardCommentUpdateReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1,"성공", result));
    }

    // 댓글,대댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.debug("commentId = {}", commentId);
        int result = commentService.deleteComment(commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 대댓글 저장
    @PostMapping("{commentId}/reply")
    public ResponseEntity<?> saveReply(
            @RequestBody BoardReplySaveReqDto boardReplySaveReqDto) {
        log.debug("boardReplyReqDto = {} ", boardReplySaveReqDto);
        BoardReplySaveRespDto result = commentService.saveReplyComment(boardReplySaveReqDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
