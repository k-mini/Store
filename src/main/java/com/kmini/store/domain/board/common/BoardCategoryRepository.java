package com.kmini.store.domain.board.common;

import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    @Modifying
    @Query("delete from BoardCategory b where b.board = :board")
    void deleteByBoard(@Param("board") Board board);

    @Query("select b from BoardCategory b join fetch b.category where b.board.id in :boardIds")
    List<BoardCategory> findByBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query("delete from BoardCategory b where b.board = :board and b.category.superCategory = null")
    void deleteSubCategoryByBoard(Board board);
}
