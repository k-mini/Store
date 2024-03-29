package com.kmini.store.service;

import com.kmini.store.config.auth.AccountContext;
import com.kmini.store.config.file.UserFileTestingManager;
import com.kmini.store.config.file.UserResourceManager;
import com.kmini.store.domain.*;
import com.kmini.store.dto.response.CommunityBoardDto.CommunityBoardViewRespDto;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.board.CommunityBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityBoardService {

    private final CommunityBoardRepository communityBoardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final UserResourceManager userFileManager;

    @Transactional
    public void save(CommunityBoard newCommunityBoard) throws IOException {
        Assert.notNull(newCommunityBoard.getSubCategoryName(),"하위 카테고리가 존재하지 않습니다.");

        AccountContext accountContext = (AccountContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = accountContext.getUsername();
        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = newCommunityBoard.getFile();
        String uri = null;
        uri = userFileManager.storeFile(username, file);

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryName("COMMUNITY")
                .orElseThrow(()->new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryName(newCommunityBoard.getSubCategoryName().toUpperCase())
                .orElseThrow(()->new IllegalArgumentException("하위 카테고리가 존재하지 않습니다."));

        newCommunityBoard.setThumbnail(uri);
        newCommunityBoard.setUser(accountContext.getUser());
        communityBoardRepository.save(newCommunityBoard);

        // 상위 카테고리 정보 저장
        BoardCategory boardCategory = new BoardCategory(newCommunityBoard, category);
        boardCategoryRepository.save(boardCategory);

        // 하위 카테고리 정보 저장
        BoardCategory boardSubCategory = new BoardCategory(newCommunityBoard, subCategory);
        boardCategoryRepository.save(boardSubCategory);
    }

    @Transactional
    public CommunityBoardViewRespDto viewBoard(Long id) {

        // 게시물 조회
        CommunityBoard board = communityBoardRepository.findByIdFetchJoin(id)
                .orElseThrow(()-> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        // 상위 댓글 조회
        List<Comment> comments = board.getComments()
                .stream()
                .filter(comment -> comment.getTopComment() == null)
                .collect(Collectors.toList());

        // 현재 조회수
        long views = board.getViews();
        // 조회수 증가 => 동시성 문제
        board.setViews(views + 1);

        return CommunityBoardViewRespDto.toDto(board, comments);
    }

    // 게시물 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        CommunityBoard communityBoard = communityBoardRepository.findByIdFetchJoin(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
        User user = communityBoard.getUser();

        // 자식 댓글 삭제 진행
        commentRepository.deleteSubCommentsByBoardId(boardId);
        // 부모 댓글 삭제 진행
        commentRepository.deleteTopCommentsByBoardId(boardId);

        // 게시물-카테고리 삭제 진행
        boardCategoryRepository.deleteByBoard(communityBoard);

        // 게시물 삭제
        communityBoardRepository.delete(communityBoard);
    }
}
