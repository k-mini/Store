package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.CommentDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentReqDto;
import com.kmini.store.dto.request.CommentDto.BoardReplyReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentRespDto;
import com.kmini.store.dto.response.CommentDto.BoardReplyRespDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
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
    public BoardCommentRespDto saveComment(BoardCommentReqDto boardCommentReqDto, User user) {

        Board board = boardRepository.getReferenceById(boardCommentReqDto.getBoardId());
        String content = boardCommentReqDto.getContent();

        // 댓글 엔티티 생성
        Comment comment = new Comment(user, board, null, content);
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return BoardCommentRespDto.toDto(savedComment);
    }

    @Transactional
    public BoardReplyRespDto saveReply(BoardReplyReqDto boardReplyReqDto, User user) {

        Board board = boardRepository.getReferenceById(boardReplyReqDto.getBoardId());
        Comment topComment = commentRepository.getReferenceById(boardReplyReqDto.getTopCommentId());
        String content = boardReplyReqDto.getContent();

        // 대댓글 엔티티 생성
        Comment reply = new Comment(user, board, topComment, content);
        // 대댓글 저장
        Comment savedReply = commentRepository.save(reply);
        return BoardReplyRespDto.toDto(savedReply);
    }

    @Transactional
    public void delete(Long commentId, Long commentUserId) {
//        User commentUser = userRepository.getReferenceById(commentUserId);
        // 댓글 삭제
        int result = commentRepository.deleteByIdAndUser(commentId, commentUserId);
        if (result == 0) {
            throw new IllegalArgumentException("삭제 실패!");
        }
    }
}
