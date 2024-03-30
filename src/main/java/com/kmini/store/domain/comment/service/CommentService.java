package com.kmini.store.domain.comment.service;

import com.kmini.store.domain.comment.repository.CommentRepository;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.entity.Comment;
import com.kmini.store.domain.entity.User;
import com.kmini.store.domain.admin.comment.dto.AdminCommentResponseDto.AdminCommentDto;
import com.kmini.store.domain.board.common.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Page<AdminCommentDto> selectAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable).map(AdminCommentDto::toDto);
    }

    @Transactional
    public Comment saveComment(Long boardId, Long topCommentId, Comment newComment) {

        User user = User.getSecurityContextUser();

        Board board = boardRepository.getReferenceById(boardId);

        Comment topComment = null;
        if (topCommentId != null) {
            topComment = commentRepository.getReferenceById(topCommentId);
        }

        // 댓글 엔티티 생성
        newComment.setBoard(board);
        newComment.setTopComment(topComment);
        // 댓글 저장
        Comment savedComment = commentRepository.save(newComment);

        return savedComment;
    }

    @Transactional
    public Comment updateComment(Long commentId, String content) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setContent(content);

        return comment;
    }

    // 댓글 여러 개 삭제
    @Transactional
    public int deleteComments(List<Long> commentIds) {

        int result = 0;

        // 자식 댓글 삭제
        result = commentRepository.deleteSubCommentsFromMultiCommentId(commentIds);

        // 댓글 삭제
        result += commentRepository.deleteCommentsFromMultiCommentId(commentIds);

        log.debug("result = {}", result);
        if (result == 0) {
            throw new IllegalArgumentException("삭제 실패!");
        }

        return result;
    }

    // 특정 댓글 삭제
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

    // 특정 게시물 댓글 조회
    @Transactional
    public List<Comment> selectTopCommentsInBoard(Long boardId) {
        return commentRepository.findAllCommentsByBoardIdFetchJoin(boardId)
                .stream()
                .filter(comment -> comment.getTopComment() == null)
                .collect(Collectors.toList());
    }
}
