package com.kmini.store.repository;

import com.kmini.store.domain.Trade;

import java.util.Optional;

public interface CustomTradeRepository {

    Optional<Trade> getLatestTrade(Long boardId);
}
