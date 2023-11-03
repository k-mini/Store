package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.FileUploader;
import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.Comment;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.BoardType;
import com.kmini.store.dto.ItemBoardRespDto.ItemBoardRespAllDto;
import com.kmini.store.dto.ItemBoardRespDto.ItemBoardRespDetailDto;
import com.kmini.store.dto.ItemBoardUpdateDto;
import com.kmini.store.dto.ItemBoardUploadDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.ex.CustomCategoryNotFoundException;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.ItemBoardRepository;
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
public class ItemBoardService {

    private final ItemBoardRepository itemBoardRepository;
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
        ItemBoard itemBoard = itemBoardRepository.findById(id)
                .orElseThrow(() -> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));

        // 댓글 조회
        List<Comment> comments = itemBoard.getComments();

        // 현재 조회수 
        int views = itemBoard.getViews();
        // 조회수 증가 => 동시성 문제
        itemBoard.setViews(views + 1);
        
        return ItemBoardRespDetailDto.toDto(itemBoard, comments);
    }

    // 게시물 저장
    @Transactional
    public void upload(ItemBoardUploadDto itemBoardUploadDto, PrincipalDetail principalDetail) throws IOException {
        
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = itemBoardUploadDto.getFile();
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
        ItemBoard itemBoard = itemBoardUploadDto.toEntity(randomName);
        itemBoard.setCategory(category);
        itemBoard.setUser(user);

        itemBoardRepository.save(itemBoard);
    }

    // 게시물 수정
    @Transactional
    public void update(ItemBoardUpdateDto itemBoardUpdateDto) {

        ItemBoard itemBoard = itemBoardRepository.findById(itemBoardUpdateDto.getBoardId())
                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        BoardCategory category = boardCategoryRepository.findById(itemBoardUpdateDto.getCategoryId())
                .orElseThrow(()->new CustomCategoryNotFoundException("카테고리가 존재하지 않습니다."));

        itemBoard.setContent(itemBoardUpdateDto.getContent());
        itemBoard.setItemName(itemBoardUpdateDto.getItemName());
        itemBoard.setThumbnail(itemBoardUpdateDto.getThumbnail());
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
    } 
}
