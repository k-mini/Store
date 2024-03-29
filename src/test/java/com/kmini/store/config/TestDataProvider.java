package com.kmini.store.config;

import com.kmini.store.domain.Comment;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.dto.request.BoardDto;
import com.kmini.store.dto.request.CommentDto.BoardCommentSaveReqDto;
import com.kmini.store.dto.request.ItemBoardDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardSaveReqDto;
import com.kmini.store.dto.response.CommentDto.BoardCommentSaveRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardSaveRespDto;
import com.kmini.store.service.CommentService;
import com.kmini.store.service.ItemBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.io.FileInputStream;

@Component
public class TestDataProvider {

    @Autowired
    ItemBoardService itemBoardService;
    @Autowired
    CommentService commentService;
    @Autowired
    EntityManager entityManager;

    public ItemBoardSaveRespDto createTestItemBoard(String subCategory, String title, String content, String itemName) throws Exception {
        String category = "trade";
        String fileName = "testImage";
        String contentType = "png";
        FileInputStream inputStream = new FileInputStream(".\\docs\\test\\" + fileName + "." + contentType);
        MockMultipartFile existingFile = new MockMultipartFile("file", fileName + "." + contentType, contentType, inputStream);
        ItemBoardSaveReqDto itemBoardSaveReqDto = new ItemBoardSaveReqDto(title, content, existingFile, itemName, null, null);
        ItemBoard itemBoard = ItemBoard.builder()
                .title(itemBoardSaveReqDto.getTitle())
                .content(itemBoardSaveReqDto.getContent())
                .file(itemBoardSaveReqDto.getFile())
                .itemName(itemBoardSaveReqDto.getItemName())
                .subCategoryName(subCategory)
                .build();
        ItemBoardSaveRespDto itemBoardSaveRespDto = itemBoardService.saveBoard(itemBoard);
        entityManager.clear();
        return itemBoardSaveRespDto;
    }

    public BoardCommentSaveRespDto createTestComment(Long boardId, Long topCommentId, String commentContent) {
        Comment newComment = new Comment(User.getSecurityContextUser(), null, null, commentContent);
        Comment savedComment = commentService.saveComment(boardId, topCommentId, newComment);
        BoardCommentSaveRespDto result = BoardCommentSaveRespDto.toDto(savedComment);
        entityManager.clear();
        return result;
    }
}
