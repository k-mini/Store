package com.kmini.store.domain.board.common.service;

import com.kmini.store.domain.board.common.BoardCategoryRepository;
import com.kmini.store.domain.board.common.repository.BoardRepository;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.board.common.dto.SearchDto.SearchBoardDto;
import com.kmini.store.domain.admin.board.dto.AdminBoardResponseDto.AdminBoardDto;
import com.kmini.store.domain.board.common.repository.BoardRepositoryImpl.BoardSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
