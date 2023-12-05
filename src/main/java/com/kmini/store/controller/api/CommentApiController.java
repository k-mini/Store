package com.kmini.store.controller.api;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplyRespDto;
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

    @PostMapping
    public ResponseEntity<?> saveComment(
            @RequestBody BoardCommentSaveReqDto boardCommentSaveReqDto) {
        log.info("boardCommentReqDto = {}", boardCommentSaveReqDto);
        BoardCommentRespDto result = commentService.saveComment(boardCommentSaveReqDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@RequestBody BoardCommentUpdateReqDto boardCommentUpdateReqDto) {
        log.debug("boardCommentUpdateReqDto = {}", boardCommentUpdateReqDto);
        BoardCommentRespDto result = commentService.updateComment(boardCommentUpdateReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1,"성공", result));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.info("commentId = {}", commentId);
        int result = commentService.deleteComment(commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    @PostMapping("{commentId}/reply")
    public ResponseEntity<?> saveReply(
            @RequestBody BoardReplySaveDto boardReplySaveDto) {
        log.info("boardReplyReqDto = {} ", boardReplySaveDto);
        BoardReplyRespDto result = commentService.saveReplyComment(boardReplySaveDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
