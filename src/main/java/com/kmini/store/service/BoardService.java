package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.dto.request.SearchDto.SearchBoardDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.repository.board.BoardRepository;
import com.kmini.store.repository.board.BoardRepositoryImpl.BoardSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<BoardDto> viewPosts(Pageable pageable, String categoryName, String subCategoryName, SearchBoardDto searchBoardDto) {

        // 검색 조건 만들기
        categoryName = categoryName.toUpperCase();
        subCategoryName = subCategoryName.toUpperCase();
        BoardSearchCond boardSearchCond = new BoardSearchCond(categoryName, subCategoryName, searchBoardDto);

        Page<Board> rawResult = boardRepository.findBoards(boardSearchCond, pageable);
        return rawResult.map(BoardDto::toDto);
    }
}
