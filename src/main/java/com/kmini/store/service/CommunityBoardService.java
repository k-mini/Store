package com.kmini.store.service;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.config.file.SystemFileManager;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.request.BoardDto.CommunityBoardFormSaveDto;
import com.kmini.store.dto.response.CommunityBoardDto.CommunityBoardRespDetailDto;
import com.kmini.store.repository.BoardCategoryRepository;
import com.kmini.store.repository.CategoryRepository;
import com.kmini.store.repository.CommentRepository;
import com.kmini.store.repository.board.CommunityBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final SystemFileManager systemFileManager;

    @Transactional
    public void save(CommunityBoardFormSaveDto communityBoardFormSaveDto, PrincipalDetail principalDetail) throws IOException {

        // 파일 시스템에 저장하고 랜덤 파일명 반환
        MultipartFile file = communityBoardFormSaveDto.getFile();
        String uri = null;
        if (file != null) {
            uri = systemFileManager.storeFile(file);
        }

        // 카테고리 조회
        Category category = categoryRepository.findByCategoryType(CategoryType.COMMUNITY)
                .orElseThrow(()->new IllegalArgumentException("상위 카테고리가 존재하지 않습니다."));
        Category subCategory = categoryRepository.findByCategoryType(communityBoardFormSaveDto.getSubCategoryType())
                .orElseThrow(()->new IllegalArgumentException("하위 카테고리가 존재하지 않습니다."));

        CommunityBoard board = communityBoardFormSaveDto.toEntity();
        board.setThumbnail(uri);
        board.setUser(principalDetail.getUser());
        communityBoardRepository.save(board);

        // 상위 카테고리 정보 저장
        BoardCategory boardCategory = new BoardCategory(board, category);
        boardCategoryRepository.save(boardCategory);

        // 하위 카테고리 정보 저장
        BoardCategory boardSubCategory = new BoardCategory(board, subCategory);
        boardCategoryRepository.save(boardSubCategory);
    }

    @Transactional
    public CommunityBoardRespDetailDto detail(Long id) {

        // 게시물 조회
        CommunityBoard board = communityBoardRepository.findByIdWithFetchJoin(id)
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

        return CommunityBoardRespDetailDto.toDto(board, comments);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long boardId) {
        CommunityBoard communityBoard = communityBoardRepository.findByIdWithFetchJoin(boardId)
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
