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
    public Page<Board> viewBoardList(Pageable pageable, SearchBoardDto searchBoardDto) {

        // 검색 조건 만들기
        String categoryName = searchBoardDto.getCategory().toUpperCase();
        String subCategoryName = searchBoardDto.getSubCategory().toUpperCase();
        BoardSearchCond boardSearchCond = new BoardSearchCond(categoryName, subCategoryName, searchBoardDto);

        return boardRepository.findBoards(boardSearchCond, pageable);
    }
}
