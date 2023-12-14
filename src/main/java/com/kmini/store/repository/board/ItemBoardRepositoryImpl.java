package com.kmini.store.repository.board;

import com.kmini.store.dto.response.ItemBoardDto.ItemBoardViewRespDto;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QComment.comment;
import static com.kmini.store.domain.QItemBoard.itemBoard;
import static com.kmini.store.domain.QTrade.trade;

@RequiredArgsConstructor
public class ItemBoardRepositoryImpl implements ItemBoardRepositoryQsdl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ItemBoardViewRespDto> findDetailById(Long id) {

        ItemBoardViewRespDto itemBoardViewRespDto = queryFactory
                .select(Projections.fields(ItemBoardViewRespDto.class,
                        itemBoard.id,
                        itemBoard.user.email,
                        itemBoard.user.id.as("writerId"),
                        itemBoard.user.username,
                        itemBoard.user.thumbnail.as("userThumbnail"),
                        itemBoard.title,
                        itemBoard.thumbnail.as("boardThumbnail"),
                        itemBoard.content,
                        Expressions.stringTemplate(
                                "formatdatetime({0},{1})",itemBoard.createdDate, "yyyy. MM. dd HH:mm"),
                        ExpressionUtils.as(JPAExpressions.select(comment.count())
                                                         .from(comment)
                                                         .where(comment.board.id.eq(itemBoard.id)),"views"),
                        ExpressionUtils.as(JPAExpressions.select(trade.tradeStatus.stringValue())
                                        .from(trade)
                                        .limit(1)
                                        .orderBy(trade.createdDate.desc()),"tradeStatus")
                ))
                .from(itemBoard)
//                .leftJoin(itemBoard.comments).fetchJoin()
//                .join(itemBoard.user, user).fetchJoin()
                .join(itemBoard.user)
                .where(itemBoard.id.eq(id))
                .fetchOne();
        
        queryFactory.select(Expressions.stringTemplate("cast({0} as character varying)",trade.id))
                .from(trade)
                .limit(1)
                .fetchOne();
        return Optional.ofNullable(itemBoardViewRespDto);
    }


}
