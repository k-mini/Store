package com.kmini.store.domain.board.common.repository;

import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.board.common.repository.BoardRepositoryImpl.BoardSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryQsdl {

    Page<Board> findBoards(BoardSearchCond cond, Pageable pageable);
}
