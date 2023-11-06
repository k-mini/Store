package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.CommentDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentResDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.ex.CustomUserNotFoundException;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public BoardCommentResDto save(BoardCommentReqDto boardCommentReqDto, User user) {

        Board board = boardRepository.findById(boardCommentReqDto.getBoardId())
                .orElseThrow(() -> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        String content = boardCommentReqDto.getContent();
        // 댓글 엔티티 생성
        Comment comment = new Comment(user, board, content);
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
        return BoardCommentResDto.toDto(savedComment);
    }

    @Transactional
    public void delete(Long commentId, Long commentUserId) {

        // 댓글 삭제
        int result = commentRepository.deleteByIdAndUser(commentId, commentUserId);
        if (result == 0) {
            throw new IllegalArgumentException("삭제 실패!");
        }
    }
}
