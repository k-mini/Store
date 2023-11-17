package com.kmini.store.repository.board;

import com.kmini.store.domain.*;
import com.kmini.store.domain.type.TradeStatus;
import com.kmini.store.dto.response.ItemBoardDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QComment.comment;
import static com.kmini.store.domain.QItemBoard.itemBoard;
import static com.kmini.store.domain.QTrade.trade;
import static com.kmini.store.domain.QUser.user;
import static com.kmini.store.domain.type.TradeStatus.WAIT;
import static com.querydsl.core.types.ExpressionUtils.count;

@RequiredArgsConstructor
public class ItemBoardRepositoryImpl implements ItemBoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<ItemBoard> findByIdWithUserAndComments(Long id) {
        ItemBoard board = queryFactory
                .selectFrom(itemBoard)
                .leftJoin(itemBoard.comments).fetchJoin()
                .join(itemBoard.user, user).fetchJoin()
                .where(itemBoard.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(board);
    }

    @Override
    public Optional<ItemBoardRespDetailDto> findDetailById(Long id) {

        ItemBoardRespDetailDto itemBoardRespDetailDto = queryFactory
                .select(Projections.fields(ItemBoardRespDetailDto.class,
                        itemBoard.id,
                        itemBoard.user.email,
                        itemBoard.user.id.as("writerId"),
                        itemBoard.user.username,
                        itemBoard.user.thumbnail.as("userThumbnail"),
                        itemBoard.title,
                        itemBoard.thumbnail.as("boardThumbnail"),
                        itemBoard.content,
//                        Expressions.stringTemplate(
//                                "formatdatetime({0},{1})",itemBoard.createdDate, "yyyy. MM. dd HH:mm"),
                        ExpressionUtils.as(JPAExpressions.select(comment.count())
                                        .from(comment)
                                        .where(comment.board.id.eq(itemBoard.id)),"views"),
                        ExpressionUtils.as(JPAExpressions.select(trade.tradeStatus.stringValue())
                                .from(trade)
                                .limit(1)
                                .orderBy(trade.createdDate.desc()), "tradeStatus")
                ))
                .from(itemBoard)
//                .leftJoin(itemBoard.comments).fetchJoin()
//                .join(itemBoard.user, user).fetchJoin()
                .where(itemBoard.id.eq(id))
                .join(itemBoard.user)
                .fetchOne();

        return Optional.ofNullable(itemBoardRespDetailDto);
    }


}
