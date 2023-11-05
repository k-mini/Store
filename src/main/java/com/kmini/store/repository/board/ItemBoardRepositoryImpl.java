package com.kmini.store.repository.board;

import com.kmini.store.domain.ItemBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QItemBoard.itemBoard;

@RequiredArgsConstructor
public class ItemBoardRepositoryImpl implements ItemBoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<ItemBoard> findCustomById(Long id) {
        ItemBoard board = queryFactory
                .selectFrom(itemBoard)
                .leftJoin(itemBoard.comments).fetchJoin()
                .where(itemBoard.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(board);
    }
}
