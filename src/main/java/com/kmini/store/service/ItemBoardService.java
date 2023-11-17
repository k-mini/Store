package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.ResourceManager;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.kmini.store.ex.CustomBoardNotFoundException;
import com.kmini.store.ex.CustomCategoryNotFoundException;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemBoardService {

    private final ItemBoardRepository itemBoardRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ResourceManager resourceManager;

    // 게시물 상세 조회
    @Transactional
    public ItemBoardRespDetailDto detail(Long id) {

        // 게시물 조회
        ItemBoard board = itemBoardRepository.findByIdWithUserAndComments(id)
                .orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        itemBoardRepository.findDetailById(id);

        // 상위 댓글 조회
        List<Comment> comments = board.getComments()
                .stream()
                .filter(comment -> comment.getTopComment() == null)
                .collect(Collectors.toList());

        // 현재 조회수 
        Long views = board.getViews();
        // 조회수 증가 => 동시성 문제
        board.setViews(views + 1);
        
        return ItemBoardRespDetailDto.toDto(board, comments);
    }

    // 게시물 저장
    @Transactional
    public void save(ItemBoardFormSaveDto itemBoardFormSaveDto, PrincipalDetail principalDetail) throws IOException {
        
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = itemBoardFormSaveDto.getFile();
        String uri = null;
        if (file != null) {
            uri = resourceManager.storeFile(file);
        }

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryType(CategoryType.TRADE)
                .orElseThrow(()->new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryType(itemBoardFormSaveDto.getSubCategoryType())
                .orElseThrow(()->new IllegalArgumentException("하위 카테고리가 존재하지 않습니다."));

        // 게시물 저장
        ItemBoard board = itemBoardFormSaveDto.toEntity();
        board.setUser(principalDetail.getUser());
        board.setThumbnail(uri);
        itemBoardRepository.save(board);

        // 상위 카테고리 정보 저장
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryRepository.save(boardCategory);

        // 하위 카테고리 정보 저장
        BoardCategory boardSubCategory = new BoardCategory(board, subCategory);
        boardCategoryRepository.save(boardSubCategory);
    }

    // 게시물 수정
    @Transactional
    public void update(ItemBoardUpdateFormDto itemBoardUpdateFormDto) {

        ItemBoard itemBoard = itemBoardRepository.getReferenceById(itemBoardUpdateFormDto.getBoardId());

        MultipartFile submittedFile = itemBoardUpdateFormDto.getFile();
        if (!submittedFile.isEmpty()) {
            resourceManager.deleteFile(itemBoard.getThumbnail());
            String updatedUri = resourceManager.storeFile(submittedFile);
            itemBoard.setThumbnail(updatedUri);
        }

        itemBoard.setContent(itemBoardUpdateFormDto.getContent());
        itemBoard.setItemName(itemBoardUpdateFormDto.getItemName());
    }

    // 게시물 수정 폼 로딩
    @Transactional
    public ItemBoardUpdateFormDto getUpdateForm(Long boardId) {
        ItemBoard itemBoard = itemBoardRepository.findByIdWithUserAndComments(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        return ItemBoardUpdateFormDto.toDto(itemBoard);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long boardId) {
        ItemBoard itemBoard = itemBoardRepository.findByIdWithUserAndComments(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        User user = itemBoard.getUser();
        // 자식 댓글 삭제 진행
        commentRepository.deleteSubCommentsByBoardId(boardId);
        // 부모 댓글 삭제 진행
        commentRepository.deleteTopCommentsByBoardId(boardId);

        // 게시물-카테고리 삭제 진행
        boardCategoryRepository.deleteByBoard(itemBoard);

        // 게시물 삭제
        itemBoardRepository.delete(itemBoard);
    }
}
