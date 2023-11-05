package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.FileUploader;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.BoardDto.FormSaveDto;
import com.kmini.store.dto.request.SearchDto.SearchBoardListDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.dto.search.BoardSearchCond;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final FileUploader fileUploader;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<BoardDto> load(Pageable pageable, String categoryName, String subCategoryName, SearchBoardListDto searchBoardListDto) {

        // 검색 조건 만들기
        CategoryType categoryType = CategoryType.valueOf(categoryName.toUpperCase());
        CategoryType subCategoryType = CategoryType.valueOf(subCategoryName.toUpperCase());
        BoardSearchCond boardSearchCond = new BoardSearchCond(categoryType, subCategoryType, searchBoardListDto);


//        Page<Board> rawResult = boardRepository.findBydtype(pageable, categoryType.getDtype());
        Page<Board> rawResult = boardRepository.findByCategories(boardSearchCond, pageable);

        return rawResult.map(BoardDto::toDto);
    }

    // 게시물 저장
    @Transactional
    public void save(FormSaveDto formSaveDto, PrincipalDetail principalDetail) throws IOException {
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = formSaveDto.getFile();
        String randomName = null;
        if (file != null) {
            fileUploader.storeFile(file);
        }

        // 카테고리 조회
//        BoardCategory category = boardCategoryRepository.findByBoardType(CategoryType.TRADE)
//                .orElseThrow(()->new CustomCategoryNotFoundException("카테고리가 존재하지 않습니다."));
        // 유저 조회
        User user = principalDetail.getUser();

        // 엔티티로 변환
        ItemBoard itemBoard = formSaveDto.toEntity(randomName);
//        itemBoard.setCategory(category);
        itemBoard.setUser(user);

        boardRepository.save(itemBoard);
    }
}
