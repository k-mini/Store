package com.kmini.store.controller.api;


import com.kmini.store.domain.Board;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardSaveReqApiDto;
import com.kmini.store.dto.request.ItemBoardDto.ItemBoardUpdateReqApiDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardDeleteRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardSaveRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardUpdateRespDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/board/trade/{subCategory}")
@RequiredArgsConstructor
@Slf4j
public class ItemBoardApiController {

    private final ItemBoardService itemBoardService;
    private static final String category = "trade";

    // 게시물 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<?> viewBoard(
            @PathVariable("subCategory") String subCategory, @PathVariable("boardId") Long boardId) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);

        ItemBoardViewRespDto result = itemBoardService.viewBoard(boardId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 게시물 생성
    @PostMapping
    public ResponseEntity<?> saveBoard(
            @PathVariable("subCategory") String subCategory,
            @RequestPart ItemBoardSaveReqApiDto itemBoardSaveReqApiDto,
            @RequestPart(required = false) MultipartFile file,
            @RequestPart(required = false) List<MultipartFile> itemImages) {
        log.debug("itemBoardSaveReqApiDto = {}", itemBoardSaveReqApiDto);
        log.debug("itemImages = {}", itemImages);

        ItemBoard savingBoard = ItemBoard.builder()
                .title(itemBoardSaveReqApiDto.getTitle())
                .content(itemBoardSaveReqApiDto.getContent())
                .itemName(itemBoardSaveReqApiDto.getItemName())
                .subCategoryName(subCategory)
                .file(file)
                .itemImageFiles(itemImages !=  null ? itemImages : new ArrayList<>())
                .latitude(itemBoardSaveReqApiDto.getLatitude())
                .longitude(itemBoardSaveReqApiDto.getLongitude())
                .build();

        ItemBoardSaveRespDto result = itemBoardService.saveBoard(savingBoard);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }


    // 게시물 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);

        Board board = itemBoardService.deleteBoard(boardId);

        ItemBoardDeleteRespDto result = ItemBoardDeleteRespDto.toDto(board);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 게시물 수정
    @PatchMapping(value = "/{boardId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateBoard(@PathVariable String subCategory, @PathVariable Long boardId,
                                         @RequestPart @Validated ItemBoardUpdateReqApiDto itemBoardUpdateReqApiDto, BindingResult bindingResult,
                                         @RequestPart(required = false) MultipartFile file,
                                         @RequestPart(required = false) List<MultipartFile> itemImages,
                                         Model model) {
        log.debug("itemBoardUpdateReqApiDto = {}", itemBoardUpdateReqApiDto);

        ItemBoard editingItemBoard = ItemBoard.builder()
                .id(boardId)
                .itemName(itemBoardUpdateReqApiDto.getItemName())
                .title(itemBoardUpdateReqApiDto.getTitle())
                .content(itemBoardUpdateReqApiDto.getContent())
                .file(file)
                .itemImageFiles(itemImages)
                .latitude(itemBoardUpdateReqApiDto.getLatitude())
                .longitude(itemBoardUpdateReqApiDto.getLongitude())
                .build();

        ItemBoard itemBoard = itemBoardService.patchBoard(editingItemBoard, itemBoardUpdateReqApiDto.getSubCategory());

        ItemBoardUpdateRespDto result = ItemBoardUpdateRespDto.toDto(itemBoard);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
