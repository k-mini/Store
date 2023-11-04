package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.FileUploader;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.BoardType;
import com.kmini.store.dto.request.ItemBoardDto.UploadDto;
import com.kmini.store.dto.response.BoardDto;
import com.kmini.store.dto.response.ItemBoardDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.ex.CustomCategoryNotFoundException;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final FileUploader fileUploader;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<BoardDto> load(Pageable pageable, String category, String subCategory) {

        BoardType categoryType = BoardType.valueOf(category.toUpperCase());
        BoardType subCategoryType = BoardType.valueOf(subCategory.toUpperCase());
        Page<Board> rawResult = boardRepository.findBydtype(pageable, categoryType.getDtype());
//        Page<Board> rawResult = boardRepository.findAll(pageable);

        return rawResult.map(BoardDto::toDto);
    }

    // 게시물 저장
    @Transactional
    public void save(UploadDto uploadDto, PrincipalDetail principalDetail) throws IOException {
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = uploadDto.getFile();
        String randomName = null;
        if (file != null) {
            fileUploader.storeFile(file);
        }

        // 카테고리 조회
        BoardCategory category = boardCategoryRepository.findByBoardType(BoardType.TRADE)
                .orElseThrow(()->new CustomCategoryNotFoundException("카테고리가 존재하지 않습니다."));
        // 유저 조회
        User user = principalDetail.getUser();

        // 엔티티로 변환
        ItemBoard itemBoard = uploadDto.toEntity(randomName);
        itemBoard.setCategory(category);
        itemBoard.setUser(user);

        boardRepository.save(itemBoard);
    }
}
