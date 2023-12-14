package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentSaveRespDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentUpdateRespDto;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardCommentSaveRespDto saveComment(BoardCommentSaveReqDto boardCommentSaveReqDto) {

        User user = User.getSecurityContextUser();

        Board board = boardRepository.getReferenceById(boardCommentSaveReqDto.getBoardId());

        Long topCommentId = boardCommentSaveReqDto.getTopCommentId();
        Comment topComment = null;
        if (topCommentId != null) {
            topComment = commentRepository.getReferenceById(topCommentId);
        }
        String content = boardCommentSaveReqDto.getContent();

        // 댓글 엔티티 생성
        Comment comment = new Comment(user, board, topComment, content);
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return BoardCommentSaveRespDto.toDto(savedComment);
    }

    @Transactional
    public BoardCommentUpdateRespDto updateComment(BoardCommentUpdateReqDto boardCommentUpdateReqDto, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setContent(boardCommentUpdateReqDto.getContent());

        return BoardCommentUpdateRespDto.toDto(comment);
    }

    @Transactional
    public int deleteComment(Long commentId) {

        // 자식 댓글 삭제
        int result = commentRepository.deleteSubComments(commentId);

        // 댓글 삭제
        result += commentRepository.deleteCommentById(commentId);

        log.debug("result = {}", result);
        if (result == 0) {
            throw new IllegalArgumentException("삭제 실패!");
        }
        return result;
    }

    @Transactional
    public void deleteAllCommentInBoard(Long boardId) {
        // 자식 댓글 삭제 진행
        commentRepository.deleteSubCommentsByBoardId(boardId);
        // 부모 댓글 삭제 진행
        commentRepository.deleteTopCommentsByBoardId(boardId);
    }


}
