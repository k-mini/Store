package com.kmini.store.domain.board.common;

import com.kmini.store.domain.board.common.service.BoardService;
import com.kmini.store.global.util.CustomPageUtils;
import com.kmini.store.global.util.PageAttr;
import com.kmini.store.domain.board.common.dto.SearchDto;
import com.kmini.store.domain.board.common.dto.BoardResponseDto;
import com.kmini.store.domain.board.common.dto.BoardListApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;


@RestController
@RequestMapping("/api/boards/{category}/{subCategory}")
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {

    private final BoardService boardService;

    // 카테고리별 게시물 조회
    @GetMapping
    public ResponseEntity<?> viewBoardList(
//            @PathVariable("category") String categoryName,
//            @PathVariable("subCategory") String subCategoryName,
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @ModelAttribute SearchDto.SearchBoardDto searchBoardDto,
            @RequestParam(value = "draw", required = false) Integer draw) {
        log.info("category = {}, subCategory = {}", searchBoardDto.getCategory(), searchBoardDto.getSubCategory());
        log.info("SearchBoardDto = {}", searchBoardDto);

        String order = searchBoardDto.getOrder();
        if (StringUtils.hasText(order) && CustomPageUtils.isValid(order)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, order, "createdDate"));
        } else {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(DESC, "createdDate"));
        }

        Page<BoardResponseDto> page = boardService.viewBoardList(pageable, searchBoardDto).map(BoardResponseDto::toDto);
        PageAttr pageAttr = CustomPageUtils.getPageAttr(page, 5);

        BoardListApiResponseDto result = BoardListApiResponseDto.builder()
                .draw(draw == null ? -1 : draw)
                .page(page)
                .pageAttr(pageAttr)
                .searchType(searchBoardDto.getSearchType())
                .searchKeyword(searchBoardDto.getSearchKeyword())
                .build();

        return ResponseEntity
                .ok(result);
    }
}

