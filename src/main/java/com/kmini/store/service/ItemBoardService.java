package com.kmini.store.service;

import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.*;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateReqDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardSaveRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemBoardService {

    private final ItemBoardRepository itemBoardRepository;
    private final TradeService tradeService;
    private final BoardCategoryService boardCategoryService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final UserResourceManager userResourceManager;

    // 게시물 상세 조회
    @Transactional
    public ItemBoardViewRespDto viewBoard(Long id) {

        // 게시물 조회
        ItemBoard board = itemBoardRepository.findByIdFetchJoinUserAndComments(id)
                .orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        // 상위 댓글 조회
        List<Comment> comments = board.getComments()
                .stream()
                .filter(comment -> comment.getTopComment() == null)
                .collect(Collectors.toList());

        // 최신 거래
        boolean tradePossible = tradeService.checkRegisterTradeAvailable(id);

        // 현재 조회수
        long views = board.getViews();

        // 조회수 증가 => 동시성 문제
        board.setViews(views + 1);
        
        return ItemBoardViewRespDto.toDto(board, comments, tradePossible);
    }

    // 게시물 저장
    @Transactional
    public ItemBoardSaveRespDto saveBoard(ItemBoard savingItemBoard) {

        // 유저 정보 저장
        savingItemBoard.setUser(User.getSecurityContextUser());

        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = savingItemBoard.getFile();
        String uri = null;
        if (file != null) {
            uri = userResourceManager.storeFile(User.getSecurityContextUser().getUsername(), file);
        }
        savingItemBoard.setThumbnail(uri);

        // 카테고리 조회
        Category category = categoryService.selectCategory("TRADE");
        Category subCategory = categoryService.selectCategory(savingItemBoard.getSubCategoryName().toUpperCase());

        // 상위 카테고리 정보 저장
        boardCategoryService.saveBoardCategory(savingItemBoard, category);

        // 하위 카테고리 정보 저장
        boardCategoryService.saveBoardCategory(savingItemBoard, subCategory);

        // 게시물 저장
        ItemBoard savedItemBoard = itemBoardRepository.save(savingItemBoard);

        return ItemBoardSaveRespDto.toDto(savedItemBoard);
    }

    // 게시물 수정
    // 있는 내용만 수정
    @Transactional
    public ItemBoard patchBoard(ItemBoard editingItemBoard, String editingSubCategoryName) {

        ItemBoard itemBoard = itemBoardRepository.findByIdFetchJoinUser(editingItemBoard.getId())
                .orElseThrow(() -> new IllegalStateException("게시물을 찾을 수 없습니다."));

        MultipartFile submittedFile = editingItemBoard.getFile();
        if (!submittedFile.isEmpty()) {
            userResourceManager.updateFile(itemBoard.getThumbnail(), submittedFile);
        }

        itemBoard.setTitle(editingItemBoard.getTitle());
        itemBoard.setContent(editingItemBoard.getContent());
        itemBoard.setItemName(editingItemBoard.getItemName());

        if (StringUtils.hasText(editingSubCategoryName)) {
            boardCategoryService.deleteSubCategoryInBoard(itemBoard);
            Category editingCategory = categoryService.selectCategory(editingSubCategoryName);
            boardCategoryService.saveBoardCategory(itemBoard, editingCategory);
        }

        return itemBoard;
    }

    // 게시물 수정 폼 로딩
    @Transactional
    public ItemBoardUpdateReqDto getUpdateForm(Long boardId)  {
        ItemBoard itemBoard = itemBoardRepository.findByIdFetchJoinUserAndComments(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        return ItemBoardUpdateReqDto.toDto(itemBoard);
    }

    // 게시물 삭제
    @Transactional
    public Board deleteBoard(Long boardId) {
        ItemBoard itemBoard = itemBoardRepository.findByIdFetchJoinUserAndComments(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        User user = itemBoard.getUser();

        // 댓글 삭제 진행
        commentService.deleteAllCommentInBoard(boardId);

        // 게시물-카테고리 삭제 진행
        boardCategoryService.deleteBoardCategory(itemBoard);

        // 게시물 삭제
        itemBoardRepository.delete(itemBoard);

        // 파일 삭제
        userResourceManager.deleteFile(itemBoard.getThumbnail());

        return itemBoard;
    }
}
