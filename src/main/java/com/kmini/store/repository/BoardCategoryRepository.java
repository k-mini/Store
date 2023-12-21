package com.kmini.store.repository;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    @Modifying
    @Query("delete from BoardCategory b where b.board = :board")
    void deleteByBoard(@Param("board") Board board);

    @Query("select b from BoardCategory b join fetch b.category where b.board.id in :boardIds")
    List<BoardCategory> findByBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query("delete from BoardCategory b where b.board = :board and b.category.superCategory = null")
    void deleteSubCategoryByBoard(Board board);
}
