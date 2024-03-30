package com.kmini.store.domain.board.trade.repository;

import com.kmini.store.domain.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade,Long>, TradeRepositoryQsl {

    Optional<Trade> findByBoardId(Long boardId);

    @Query("select t from Trade t join fetch t.board join fetch t.buyer where t.id = :tradeId")
    Optional<Trade> findByIdFetchJoin(@Param("tradeId") Long tradeId);
}
