package com.kmini.store.repository;

import com.kmini.store.domain.QTrade;
import com.kmini.store.domain.Trade;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QTrade.trade;

@RequiredArgsConstructor
public class TradeRepositoryImpl implements CustomTradeRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Trade> getLatestTrade(Long boardId) {
        JPAQuery<Trade> query = queryFactory.selectFrom(trade)
                                            .where(trade.board.id.eq(boardId))
                                            .orderBy(trade.createdDate.desc())
                                            .limit(1);
        return Optional.ofNullable(query.fetchOne());
    }
}
