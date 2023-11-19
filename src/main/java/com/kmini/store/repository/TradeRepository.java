package com.kmini.store.repository;

import com.kmini.store.domain.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TradeRepository extends JpaRepository<Trade,Long>, CustomTradeRepository {


    @Query("select t from Trade t where t.board.id = :boardId order by t.createdDate desc")
    Optional<Trade> getLatestTrade(@Param("boardId")Long boardId, Pageable pageable);

    Optional<Trade> findByBoardId(Long boardId);
}
