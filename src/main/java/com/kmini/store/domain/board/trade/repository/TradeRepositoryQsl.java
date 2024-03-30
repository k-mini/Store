package com.kmini.store.domain.board.trade.repository;

import com.kmini.store.domain.entity.Trade;
import com.kmini.store.domain.board.trade.dto.TradeRequestDto.SelectUserTradeHistoryReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TradeRepositoryQsl {

    Optional<Trade> getLatestTrade(Long boardId);

    Page<Trade> findTradeHistoryByUserIdAndKeyword(SelectUserTradeHistoryReqDto selectUserTradeHistoryReqDto, Pageable pageable);
}
