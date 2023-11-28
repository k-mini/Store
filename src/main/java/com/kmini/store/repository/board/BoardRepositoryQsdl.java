package com.kmini.store.repository.board;

import com.kmini.store.domain.Board;
import com.kmini.store.repository.board.BoardRepositoryImpl.BoardSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryQsdl {

    Page<Board> findBoards(BoardSearchCond cond, Pageable pageable);
}
