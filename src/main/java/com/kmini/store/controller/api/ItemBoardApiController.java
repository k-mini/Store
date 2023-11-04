package com.kmini.store.controller.api;


import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespAllDto;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.service.ItemBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/boards/trade")
@RequiredArgsConstructor
@Slf4j
public class ItemBoardApiController {

    private final ItemBoardService itemBoardService;

    // 최신 시간 순으로 조회
    @GetMapping
    public ResponseEntity<?> load(
            @PageableDefault(sort = "createdDate", direction = DESC) Pageable pageable) {
        Page<ItemBoardRespAllDto> content = itemBoardService.load(pageable);
        return ResponseEntity.ok(new CommonRespDto<Page<ItemBoardRespAllDto>>(1,"success", content));
    }
}
