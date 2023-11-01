package com.kmini.store.controller.api;


import com.kmini.store.dto.ItemBoardUploadDto;
import com.kmini.store.dto.RespDto;
import com.kmini.store.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    // 게시물 저장
    @PostMapping("/{id}")
    public ResponseEntity<?> upload(@RequestBody ItemBoardUploadDto boardUploadDto) {
        boardService.upload(boardUploadDto.toEntity());
        return ResponseEntity.ok(new RespDto<Void>(1, "성공", null));
    }
}
