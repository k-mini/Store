package com.kmini.store.controller.api;

import com.kmini.store.config.auth.PrincipalDetail;
import com.kmini.store.domain.User;
import com.kmini.store.dto.CommonRespDto;
import com.kmini.store.dto.request.TradeDto;
import com.kmini.store.dto.request.TradeDto.TradeReqDto;
import com.kmini.store.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public CommonRespDto<?> trade(TradeReqDto tradeReqDto, @AuthenticationPrincipal PrincipalDetail principal) {
        log.debug("tradeReqDto = {}", tradeReqDto);
        User buyer = principal.getUser();
        tradeReqDto.setBuyerId(buyer.getId());
        tradeService.beginTrade(tradeReqDto);
        return new CommonRespDto<>(1,"성공",null);
    }
}
