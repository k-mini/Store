package com.kmini.store.controller.api;

import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.TradeDto.TradeReqDto;
import com.kmini.store.service.impl.TradeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Slf4j
public class TradeApiController {

    private final TradeServiceImpl tradeService;

    @PostMapping("/{boardId}")
    public CommonRespDto<?> trade(TradeReqDto tradeReqDto) {
        log.debug("tradeReqDto = {}", tradeReqDto);
        Long boardId = tradeReqDto.getBoardId();
        tradeService.registerTrade(boardId);
        return new CommonRespDto<>(1,"성공",null);
    }

    // 거래 승낙
    @PatchMapping("/{tradeId}/accept")
    public CommonRespDto<?> acceptTrade(@PathVariable Long tradeId) {
        tradeService.acceptTrade(tradeId);
        return new CommonRespDto<Void>(1, "성공", null);
    }

    // 거래 거절
    @PatchMapping("/{tradeId}/deny")
    public CommonRespDto<?> denyTrade(@PathVariable Long tradeId) {
        tradeService.denyTrade(tradeId);
        return new CommonRespDto<Void>(1, "성공", null);
    }

    // 거래 완료
    @PatchMapping("/{tradeId}/complete")
    public CommonRespDto<?> completeTrade(@PathVariable Long tradeId) {
        tradeService.completeTrade(tradeId);
        return new CommonRespDto<Void>(1,"성공", null);
    }

    // 거래 취소
    @PatchMapping("/{tradeId}/cancel")
    public CommonRespDto<?> cancelTrade(@PathVariable Long tradeId) {
        tradeService.cancelTrade(tradeId);
        return new CommonRespDto<Void>(1,"성공", null);
    }
}
