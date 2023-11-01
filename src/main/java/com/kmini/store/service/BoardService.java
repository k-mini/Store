package com.kmini.store.service;

import com.kmini.store.domain.BoardCategory;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.ItemBoardUpdateDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.repository.ItemBoardRepository;
import com.kmini.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final ItemBoardRepository itemBoardRepository;

    // 게시물 저장
    @Transactional
    public void upload(ItemBoard itemBoard) {
        itemBoardRepository.save(itemBoard);
    }

    // 게시물 수정
    @Transactional
    public void update(ItemBoardUpdateDto itemBoardUpdateDto) {

        ItemBoard itemBoard = itemBoardRepository.findById(itemBoardUpdateDto.getBoardId())
                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        Long categoryId = itemBoardUpdateDto.getCategoryId();

        itemBoard.setContent(itemBoardUpdateDto.getContent());
        itemBoard.setItemName(itemBoardUpdateDto.getItemName());
        itemBoard.setThumbnail(itemBoardUpdateDto.getThumbnail());
        itemBoard.setCategory(new BoardCategory(categoryId));
    }
    
    // 게시물 삭제
    @Transactional
    public void delete(Long id) {
        ItemBoard itemBoard = itemBoardRepository.findById(id).orElseThrow(() -> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        // 댓글 삭제 진행
        
        // 게시물 삭제
        itemBoardRepository.delete(itemBoard);
    } 
    
    
}
