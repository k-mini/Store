package com.kmini.store.repository.board;

import com.kmini.store.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    Page<Board> findBydtype(Pageable pageable, String dtype);
}
