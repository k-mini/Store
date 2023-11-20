package com.kmini.store.repository;

import com.kmini.store.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade,Long>, TradeRepositoryQsl {

    Optional<Trade> findByBoardId(Long boardId);
}
