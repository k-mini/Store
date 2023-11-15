package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.CommentDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentUpdateDto;
import com.kmini.store.dto.request.CommentDto.BoardReplySaveDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplyRespDto;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public BoardCommentRespDto saveComment(BoardCommentSaveDto boardCommentSaveDto, User user) {

        Board board = boardRepository.getReferenceById(boardCommentSaveDto.getBoardId());
        String content = boardCommentSaveDto.getContent();

        // 댓글 엔티티 생성
        Comment comment = new Comment(user, board, null, content);
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return BoardCommentRespDto.toDto(savedComment);
    }

    @Transactional
    public BoardReplyRespDto saveReply(BoardReplySaveDto boardReplySaveDto, User user) {

        Board board = boardRepository.getReferenceById(boardReplySaveDto.getBoardId());
        Comment topComment = commentRepository.getReferenceById(boardReplySaveDto.getTopCommentId());
        String content = boardReplySaveDto.getContent();

        // 대댓글 엔티티 생성
        Comment reply = new Comment(user, board, topComment, content);
        // 대댓글 저장
        Comment savedReply = commentRepository.save(reply);
        return BoardReplyRespDto.toDto(savedReply);
    }
    @Transactional
    public void update(BoardCommentUpdateDto boardCommentUpdateDto) {

        Comment comment = commentRepository.findById(boardCommentUpdateDto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.setContent(boardCommentUpdateDto.getContent());
    }

    @Transactional
    public int delete(Long commentId, User commentUser) {

        // 자식 댓글 삭제
        int result = commentRepository.deleteSubComments(commentId);

        // 부모 댓글 삭제
        result += commentRepository.deleteCommentById(commentId);

        log.debug("result = {}", result);
        if (result == 0) {
            throw new IllegalArgumentException("삭제 실패!");
        }
        return result;
    }
}
