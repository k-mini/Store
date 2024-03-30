package com.kmini.store.domain.board.trade;

import com.kmini.store.domain.board.trade.dto.TradeResponseDto;
import com.kmini.store.domain.board.trade.dto.TradeResponseDto.*;
import com.kmini.store.domain.board.trade.service.TradeService;
import com.kmini.store.domain.common.dto.CommonRespDto;
import com.kmini.store.domain.board.trade.dto.TradeRequestDto.SelectUserTradeHistoryReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Slf4j
public class TradeApiController {

    private final TradeService tradeService;

    // 거래 조회
    @GetMapping("/user/{userId}/trade-history")
    public ResponseEntity<?> selectUserTradeHistory(@PageableDefault(sort="createdDate", direction = DESC) Pageable pageable,
                                                    @PathVariable Long userId,
                                                    @RequestBody SelectUserTradeHistoryReqDto selectUserTradeHistoryReqDto) {
        selectUserTradeHistoryReqDto.setUserId(userId);
        log.debug("selectUserTradeHistoryReqDto = {}" , selectUserTradeHistoryReqDto);

        Page<TradeHistoryRespDto> results = tradeService.selectUserTradeHistory(selectUserTradeHistoryReqDto, pageable);

//        return ResponseEntity.ok().body(new CommonRespDto<>(1,"성공", results));
        return ResponseEntity.ok(null);
    }

    // 거래 신청
    @PostMapping("/{boardId}")
    public ResponseEntity<?> registerTrade(@PathVariable Long boardId) {
        log.debug("boardId = {}", boardId);

        TradeRegisterRespDto result = tradeService.registerTrade(boardId);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new CommonRespDto<>(1, "성공", result));
    }

    // 거래 승낙
    @PatchMapping("/{tradeId}/accept")
    public ResponseEntity<?> acceptTrade(@PathVariable Long tradeId) {
        log.debug("tradeId = {}", tradeId);

        TradeAcceptRespDto result = tradeService.acceptTrade(tradeId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new CommonRespDto<>(1, "성공", result));
    }

    // 거래 거절
    @PatchMapping("/{tradeId}/deny")
    public ResponseEntity<?> denyTrade(@PathVariable Long tradeId) {
        log.debug("tradeId = {}", tradeId);

        TradeDenyRespDto result = tradeService.denyTrade(tradeId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(new CommonRespDto<>(1, "성공", result));
    }

    // 거래 완료
    @PatchMapping("/{tradeId}/complete")
    public ResponseEntity<?> completeTrade(@PathVariable Long tradeId) {
        log.debug("tradeId = {}", tradeId);

        TradeCompleteRespDto result = tradeService.completeTrade(tradeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }

    // 거래 취소
    @PatchMapping("/{tradeId}/cancel")
    public ResponseEntity<?> cancelTrade(@PathVariable Long tradeId) {
        log.debug("tradeId = {}", tradeId);

        TradeCancelRespDto result = tradeService.cancelTrade(tradeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommonRespDto<>(1, "성공", result));
    }
}
