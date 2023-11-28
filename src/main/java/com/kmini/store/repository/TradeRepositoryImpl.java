package com.kmini.store.repository;

import com.kmini.store.domain.Trade;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.request.TradeDto;
import com.kmini.store.dto.request.TradeDto.TradeHistoryReqDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.kmini.store.domain.QBoard.board;
import static com.kmini.store.domain.QTrade.trade;

@RequiredArgsConstructor
public class TradeRepositoryImpl implements TradeRepositoryQsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Trade> getLatestTrade(Long boardId) {
        JPAQuery<Trade> query = queryFactory.selectFrom(trade)
                .where(trade.board.id.eq(boardId))
                .orderBy(trade.createdDate.desc())
                .limit(1);
        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Page<Trade> findByHistoryByUserIdAndKeyword(TradeHistoryReqDto tradeHistoryReqDto, Pageable pageable) {

        // 조회자 Id
        Long userId = tradeHistoryReqDto.getUserId();
        // 검색 조건 만들기
        TradeHistorySearchCond cond = getSearchCond(tradeHistoryReqDto);

        JPAQuery<Trade> query =
                queryFactory.selectFrom(trade)
                        .join(trade.buyer).fetchJoin()
                        .join(trade.board).fetchJoin()
                        .where(trade.buyer.id.eq(userId).or(trade.board.user.id.eq(userId)),
                                titleLike(cond.getTitle()),
                                contentLike(cond.getContent()),
                                statusEq(cond.getStatus())
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(trade.createdDate.desc());

        JPAQuery<Long> countQuery =
                queryFactory.select(trade.count())
                        .from(trade)
                        .join(trade.buyer).fetchJoin()
                        .join(trade.board).fetchJoin()
                        .where(trade.buyer.id.eq(userId).or(trade.board.user.id.eq(userId)),
                                titleLike(cond.getTitle()),
                                contentLike(cond.getContent()),
                                statusEq(cond.getStatus())
                        );

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchOne);
    }

    private TradeHistorySearchCond getSearchCond(TradeHistoryReqDto tradeHistoryReqDto) {
        return new TradeHistorySearchCond(tradeHistoryReqDto.getS(), tradeHistoryReqDto.getSType());
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? trade.board.title.like("%" + title + "%") : null;
    }

    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? trade.board.content.like("%" + content + "%") : null;
    }

    private BooleanExpression statusEq(TradeStatus status) {
        return status != null ? trade.tradeStatus.stringValue().eq(status.name()) : null;
    }

    @Data
    public static class TradeHistorySearchCond {

        // 제목
        private String title;
        // 내용
        private String content;
        // 상태
        private TradeStatus status;

        public TradeHistorySearchCond(String s, String sType) {
            this.title = "title".equals(sType) ? s : null;
            this.content = "content".equals(sType) ? s : null;
            this.status = "status".equals(sType) ? TradeStatus.valueOf(s) : null;
        }
    }
}
