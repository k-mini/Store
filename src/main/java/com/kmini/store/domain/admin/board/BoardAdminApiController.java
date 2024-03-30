package com.kmini.store.domain.admin.board;

import com.kmini.store.domain.board.common.service.BoardService;
import com.kmini.store.domain.entity.Board;
import com.kmini.store.domain.common.dto.CommonRespDto;
import com.kmini.store.domain.board.item.dto.ItemBoardReponseDto.ItemBoardDeleteRespDto;
import com.kmini.store.domain.admin.board.dto.AdminBoardResponseDto;
import com.kmini.store.domain.admin.board.dto.AdminBoardResponseDto.AdminBoardDto;
import com.kmini.store.domain.board.community.service.CommunityBoardService;
import com.kmini.store.domain.board.item.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class BoardAdminApiController {


    private final BoardService boardService;
    private final ItemBoardService itemBoardService;
    private final CommunityBoardService communityBoardService;

    // 전체 게시물 조회
    @GetMapping("/boards")
    public ResponseEntity<?> viewBoardList(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false) Optional<Integer> draw) {

        Page<AdminBoardDto> page = boardService.viewAllBoards(pageable);

        AdminBoardResponseDto<AdminBoardDto> result = new AdminBoardResponseDto<>(draw.orElse(0), page);

        return ResponseEntity
                .ok(new CommonRespDto<>(1, "성공", result));
    }

    // 선택한 게시물 삭제
    @DeleteMapping("/boards")
    public ResponseEntity<?> deleteSelectedBoards(@RequestParam("boardIds") List<Integer> boardIDs){
        log.info("boardIDs = {}", boardIDs);

        return ResponseEntity
                .ok(new CommonRespDto<>(1,"성공", null));
    }

    // 게시물 1개 삭제
    @DeleteMapping("/board/{category}/{subCategory}/{boardId}")
    public ResponseEntity<?> deleteBoard(
            @PathVariable("category") String category,
            @PathVariable("subCategory") String subCategory,
            @PathVariable("boardId") Long boardId) {
        log.debug("category = {} subCategory = {} boardId = {}", category, subCategory, boardId);

        ItemBoardDeleteRespDto result = null ;

        if (category.equalsIgnoreCase("TRADE")) {
            Board board = itemBoardService.deleteBoard(boardId);
            result = ItemBoardDeleteRespDto.toDto(board);
        }
        else if (category.equalsIgnoreCase("COMMUNITY")){
            communityBoardService.deleteBoard(boardId);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
