package com.kmini.store.domain.comment;

import com.kmini.store.domain.comment.service.CommentService;
import com.kmini.store.domain.entity.Comment;
import com.kmini.store.domain.entity.User;
import com.kmini.store.domain.common.dto.CommonRespDto;
import com.kmini.store.domain.comment.dto.CommentRequestDto.BoardCommentSaveReqDto;
import com.kmini.store.domain.comment.dto.CommentRequestDto.BoardCommentUpdateReqDto;
import com.kmini.store.domain.comment.dto.CommentResponseDto.BoardCommentSaveRespDto;
import com.kmini.store.domain.comment.dto.CommentResponseDto.BoardCommentSelectRespDto;
import com.kmini.store.domain.comment.dto.CommentResponseDto.BoardCommentUpdateRespDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentApiController {

    private final CommentService commentService;

    // 특정 게시물 댓글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<?> selectComments(@PathVariable Long boardId) {

        List<Comment> comments = commentService.selectTopCommentsInBoard(boardId);

        List<BoardCommentSelectRespDto> result = BoardCommentSelectRespDto.toDtos(comments);

        return ResponseEntity.ok()
                             .body(new CommonRespDto<>(1,"성공", result));
    }

    // 댓글, 대댓글 저장
    @PostMapping
    public ResponseEntity<?> saveComment(
            @RequestBody BoardCommentSaveReqDto boardCommentSaveReqDto) {
        log.debug("boardCommentSaveReqDto = {}", boardCommentSaveReqDto);

        Comment newComment = new Comment(User.getSecurityContextUser(), null,null, boardCommentSaveReqDto.getContent());

        Comment savedComment = commentService.saveComment(boardCommentSaveReqDto.getBoardId(), boardCommentSaveReqDto.getTopCommentId(), newComment);

        BoardCommentSaveRespDto result = BoardCommentSaveRespDto.toDto(savedComment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 댓글, 대댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateComment( @PathVariable Long commentId,
                                            @RequestBody BoardCommentUpdateReqDto boardCommentUpdateReqDto) {
        log.debug("boardCommentUpdateReqDto = {}", boardCommentUpdateReqDto);

        Comment updatedComment = commentService.updateComment(commentId, boardCommentUpdateReqDto.getContent());
        BoardCommentUpdateRespDto result = BoardCommentUpdateRespDto.toDto(updatedComment);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1,"성공", result));
    }

    // 댓글, 대댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        log.debug("commentId = {}", commentId);
        int result = commentService.deleteComment(commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
