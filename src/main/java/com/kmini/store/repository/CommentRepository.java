package com.kmini.store.repository;

import com.kmini.store.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Comment c where c.id = :commentId and c.user.id = :commentUserId")
    int deleteByIdAndUser(@Param("commentId") Long commentId,@Param("commentUserId") Long commentUserId);
}
