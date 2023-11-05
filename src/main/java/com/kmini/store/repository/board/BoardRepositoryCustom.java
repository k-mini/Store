package com.kmini.store.repository.board;

import com.kmini.store.domain.Board;
import com.kmini.store.dto.search.BoardSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findBoards(BoardSearchCond cond, Pageable pageable);
}
