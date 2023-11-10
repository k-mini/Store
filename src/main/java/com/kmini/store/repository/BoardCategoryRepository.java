package com.kmini.store.repository;

import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.ItemBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    @Modifying
    @Query("delete from BoardCategory b where b.board = :itemBoard")
    void deleteByBoard(@Param("itemBoard") ItemBoard itemBoard);
}
