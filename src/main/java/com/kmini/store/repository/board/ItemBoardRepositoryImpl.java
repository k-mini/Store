package com.kmini.store.repository.board;

import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QItemBoard.itemBoard;
import static com.kmini.store.domain.QUser.user;

@RequiredArgsConstructor
public class ItemBoardRepositoryImpl implements ItemBoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<ItemBoard> findByIdWithFetchJoin(Long id) {
        ItemBoard board = queryFactory
                .selectFrom(itemBoard)
                .leftJoin(itemBoard.comments).fetchJoin()
                .join(itemBoard.user, user).fetchJoin()
                .where(itemBoard.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(board);
    }
}
