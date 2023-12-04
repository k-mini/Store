package com.kmini.store.service;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.*;
import com.kmini.store.dto.request.BoardDto.ItemBoardFormSaveDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateFormDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final TradeService tradeService;
    private final UserResourceManager userResourceManager;

    // 게시물 상세 조회
    @Transactional
    public ItemBoardRespDetailDto viewBoard(Long id) {

        // 게시물 조회
        ItemBoard board = itemBoardRepository.findByIdWithUserAndComments(id)
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
        
        return ItemBoardRespDetailDto.toDto(board, comments, tradePossible);
    }

    // 게시물 저장
    @Transactional
    public ItemBoard save(ItemBoardFormSaveDto itemBoardFormSaveDto) throws IOException {

        AccountContext accountContext = (AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = itemBoardFormSaveDto.getFile();
        String uri = null;
        if (file != null) {
            uri = userResourceManager.storeFile(accountContext.getUser().getEmail(), file);
        }

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryName("TRADE")
                .orElseThrow(()-> new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryName(itemBoardFormSaveDto.getSubCategory().toUpperCase())
                .orElseThrow(()-> new IllegalArgumentException("하위 카테고리가 존재하지 않습니다."));

        // 게시물 저장
        ItemBoard board = itemBoardFormSaveDto.toEntity();
        board.setUser(accountContext.getUser());
        board.setThumbnail(uri);
        ItemBoard itemBoard = itemBoardRepository.save(board);

        // 상위 카테고리 정보 저장
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryRepository.save(boardCategory);

        // 하위 카테고리 정보 저장
        BoardCategory boardSubCategory = new BoardCategory(board, subCategory);
        boardCategoryRepository.save(boardSubCategory);

        return itemBoard;
    }

    // 게시물 수정
    @Transactional
    public void updatePost(ItemBoardUpdateFormDto itemBoardUpdateFormDto) {

        ItemBoard itemBoard = itemBoardRepository.findById(itemBoardUpdateFormDto.getBoardId())
                .orElseThrow(() -> new IllegalStateException("게시물을 찾을 수 없습니다."));

        MultipartFile submittedFile = itemBoardUpdateFormDto.getFile();
        if (!submittedFile.isEmpty()) {
            userResourceManager.updateFile(itemBoard.getThumbnail(), submittedFile);
        }

        itemBoard.setTitle(itemBoardUpdateFormDto.getTitle());
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
    public void deletePost(Long boardId) {
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

        // 파일 삭제
        userResourceManager.deleteFile(itemBoard.getThumbnail());
    }
}
