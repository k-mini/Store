package com.kmini.store.repository;

import com.kmini.store.domain.Trade;
import com.kmini.store.dto.request.TradeDto.SelectUserTradeHistoryReqDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TradeRepositoryQsl {

    Optional<Trade> getLatestTrade(Long boardId);

    Page<Trade> findTradeHistoryByUserIdAndKeyword(SelectUserTradeHistoryReqDto selectUserTradeHistoryReqDto, Pageable pageable);
}
