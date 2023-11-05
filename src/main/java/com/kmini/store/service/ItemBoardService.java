package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.FileUploader;
import com.kmini.store.domain.*;
import com.kmini.store.dto.request.BoardDto.FormSaveDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespAllDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.ex.CustomCategoryNotFoundException;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemBoardService {

    private final ItemBoardRepository itemBoardRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final FileUploader fileUploader;

    // 게시물 조회 ( 디폴트 : 최신 시간 순으로)
    @Transactional(readOnly = true)
    public Page<ItemBoardRespAllDto> load(Pageable pageable) {
        Page<ItemBoard> rawResult = itemBoardRepository.findAll(pageable);
        return rawResult.map(ItemBoardRespAllDto::toDto);
    }

    // 게시물 상세 조회
    @Transactional
    public ItemBoardRespDetailDto detail(Long id) {

        // 게시물 조회
        ItemBoard board = itemBoardRepository.findCustomById(id)
                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));

        // 댓글 조회
        List<Comment> comments = board.getComments();

        // 현재 조회수 
        int views = board.getViews();
        // 조회수 증가 => 동시성 문제
        board.setViews(views + 1);
        
        return ItemBoardRespDetailDto.toDto(board, comments);
    }

    // 게시물 저장
    @Transactional
    public void save(FormSaveDto formSaveDto, PrincipalDetail principalDetail) throws IOException {
        
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = formSaveDto.getFile();
        String uri = null;
        if (file != null) {
            uri = fileUploader.storeFile(file);
        }

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryType(formSaveDto.getCategory())
                .orElseThrow(()->new CustomCategoryNotFoundException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryType(formSaveDto.getSubCategory())
                .orElseThrow(()->new CustomCategoryNotFoundException("하위 카테고리가 존재하지 않습니다."));
        // 유저 조회
        User user = principalDetail.getUser();

        // 게시물 저장
        ItemBoard board = formSaveDto.toEntity(uri);
        board.setUser(user);
        itemBoardRepository.save(board);

        // 상위 카테고리 정보 저장
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryRepository.save(boardCategory);

        // 하위 카테고리 정보 저장
        BoardCategory boardSubCategory = new BoardCategory(board, subCategory);
        boardCategoryRepository.save(boardSubCategory);
    }

    /*// 게시물 수정
    @Transactional
    public void update(UpdateDto updateDto) {

        ItemBoard itemBoard = itemBoardRepository.findById(updateDto.getBoardId())
                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        BoardCategory category = boardCategoryRepository.findById(updateDto.getCategoryId())
                .orElseThrow(()->new CustomCategoryNotFoundException("카테고리가 존재하지 않습니다."));

        itemBoard.setContent(updateDto.getContent());
        itemBoard.setItemName(updateDto.getItemName());
        itemBoard.setThumbnail(updateDto.getThumbnail());
        itemBoard.setCategory(category);
    }
    
    // 게시물 삭제
    @Transactional
    public void delete(Long id) {
        ItemBoard itemBoard = itemBoardRepository.findById(id)
                .orElseThrow(() -> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));

        // 댓글 삭제 진행
        
        // 게시물 삭제
        itemBoardRepository.delete(itemBoard);
    } */
}
