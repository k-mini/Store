package com.kmini.store.service;

import com.kmini.store.domain.Board;
import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.Category;
import com.kmini.store.dto.request.SearchDto.SearchBoardDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.dto.response.admin.AdminBoardResponseDto;
import com.kmini.store.dto.response.admin.AdminBoardResponseDto.AdminBoardDto;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.board.BoardRepository;
import com.kmini.store.repository.board.BoardRepositoryImpl.BoardSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<Board> viewBoardList(Pageable pageable, SearchBoardDto searchBoardDto) {

        // 검색 조건 만들기
        String categoryName = searchBoardDto.getCategory().toUpperCase();
        String subCategoryName = searchBoardDto.getSubCategory().toUpperCase();
        BoardSearchCond boardSearchCond = new BoardSearchCond(categoryName, subCategoryName, searchBoardDto);

        return boardRepository.findBoards(boardSearchCond, pageable);
    }

    @Transactional
    public Page<AdminBoardDto> viewAllBoards(Pageable pageable) {

        // 첫번째 쿼리
        Page<Board> pageBoard = boardRepository.findAll(pageable);

//        List<Long> boardIds = pageBoard.get()
//                .map(Board::getId)
//                .collect(Collectors.toList());
        // 두번째 쿼리
//        boardCategoryRepository.findByBoardIds(boardIds);

        return pageBoard.map(AdminBoardDto::toDto);
    }
}
