package com.kmini.store.repository;

import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.type.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    Optional<BoardCategory> findByBoardType(BoardType type);
}
