package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.FileUploader;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.BoardDto;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.BoardDto.UpdateDto;
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
    private final FileUploader fileUploader;

    // 게시물 상세 조회
    @Transactional
    public ItemBoardRespDetailDto detail(Long id) {

        // 게시물 조회
        ItemBoard board = itemBoardRepository.findByIdWithFetchJoin(id)
                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));

        // 상위 댓글 조회
        List<Comment> comments = board.getComments()
                .stream()
                .filter(comment -> comment.getTopComment() == null)
                .collect(Collectors.toList());

        // 현재 조회수 
        int views = board.getViews();
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
            uri = fileUploader.storeFile(file);
        }

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryType(CategoryType.TRADE)
                .orElseThrow(()->new CustomCategoryNotFoundException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryType(itemBoardFormSaveDto.getSubCategoryType())
                .orElseThrow(()->new CustomCategoryNotFoundException("하위 카테고리가 존재하지 않습니다."));

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
    public void update(UpdateDto updateDto) {

        ItemBoard itemBoard = itemBoardRepository.getReferenceById(updateDto.getBoardId());
//                .orElseThrow(()-> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
        BoardCategory category = boardCategoryRepository.getReferenceById(updateDto.getCategoryId());
//                .orElseThrow(()->new CustomCategoryNotFoundException("카테고리가 존재하지 않습니다."));

        itemBoard.setContent(updateDto.getContent());
        itemBoard.setItemName(updateDto.getItemName());
        itemBoard.setThumbnail(updateDto.getThumbnail());
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long boardId) {
        ItemBoard itemBoard = itemBoardRepository.findByIdWithFetchJoin(boardId)
                .orElseThrow(() -> new CustomBoardNotFoundException("게시물을 찾을 수 없습니다."));
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
