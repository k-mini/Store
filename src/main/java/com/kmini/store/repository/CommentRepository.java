package com.kmini.store.repository;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId ")
    int deleteByIdCascade(@Param("commentId") Long commentId);

    // 해당 댓글의 자식 댓글 삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.topComment.id = :commentId")
    int deleteSubComments(@Param("commentId") Long commentId);

    // 여러 부모 자식의 자식 댓글 여러개 삭제
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from Comment c where c.topComment.id in :commentIds")
    int deleteSubCommentsFromMultiCommentId(@Param("commentIds") List<Long> commentIds);

    // 해당 댓글 삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId")
    int deleteCommentById(@Param("commentId") Long commentId);

    // 여러 댓글 한꺼번에 삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.id in :commentIds")
    int deleteCommentsFromMultiCommentId(@Param("commentIds") List<Long> commentIds);

    // 해당 게시물에 해당하는 부모 댓글삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.board.id = :boardId and c.topComment = null ")
    void deleteTopCommentsByBoardId(@Param("boardId") Long boardId);

    // 해당 게시물에 해당하는 자식 댓글삭제
    @Modifying(flushAutomatically = true)
    @Query("delete from Comment c where c.board.id = :boardId and c.topComment != null ")
    void deleteSubCommentsByBoardId(@Param("boardId") Long boardId);

    @Query("select c from Comment c join fetch c.board join fetch c.user where c.board.id = :boardId")
    List<Comment> findAllCommentsByBoardIdFetchJoin(@Param("boardId") Long boardId);
}
