package com.kmini.store.domain.board.common.service;

import com.kmini.store.domain.board.common.BoardCategoryRepository;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.entity.BoardCategory;
import com.kmini.store.domain.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    @Transactional
    public BoardCategory saveBoardCategory(Board board, Category category) {
        Assert.notNull(board, "게시물 정보가 존재하지 않습니다.");
        Assert.notNull(category, "카테고리 정보가 존재하지 않습니다.");

        return boardCategoryRepository.save(new BoardCategory(board, category));
    }

    @Transactional
    public void deleteBoardCategory(Board board) {
        Assert.notNull(board, "게시물 정보가 존재하지 않습니다.");

        boardCategoryRepository.deleteByBoard(board);
    }

    @Transactional
    public void deleteSubCategoryInBoard(Board board) {
        Assert.notNull(board, "게시물 정보가 존재하지 않습니다.");

        boardCategoryRepository.deleteSubCategoryByBoard(board);
    }
}
