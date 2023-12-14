package com.kmini.store.repository;

import com.kmini.store.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId ")
    int deleteByIdCascade(@Param("commentId") Long commentId);

    // 해당 댓글의 자식 댓글 삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.topComment.id = :commentId")
    int deleteSubComments(@Param("commentId") Long commentId);

    // 해당 댓글 삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId")
    int deleteCommentById(@Param("commentId") Long commentId);

    // 해당 게시물에 해당하는 부모 댓글삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.board.id = :boardId and c.topComment = null ")
    void deleteTopCommentsByBoardId(@Param("boardId") Long boardId);

    // 해당 게시물에 해당하는 자식 댓글삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.board.id = :boardId and c.topComment != null ")
    void deleteSubCommentsByBoardId(@Param("boardId") Long boardId);
}
