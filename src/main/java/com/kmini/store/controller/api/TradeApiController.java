package com.kmini.store.controller.api;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.TradeDto;
import com.kmini.store.dto.request.TradeDto.TradeReqDto;
import com.kmini.store.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Slf4j
public class TradeApiController {

    private final TradeService tradeService;

    @PostMapping("/{boardId}")
    public CommonRespDto<?> trade(TradeReqDto tradeReqDto) {
        log.debug("tradeReqDto = {}", tradeReqDto);
        tradeService.beginTrade(tradeReqDto);
        return new CommonRespDto<>(1,"성공",null);
    }
}
