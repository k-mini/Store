package com.kmini.store.repository.board;

import com.kmini.store.domain.CommunityBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.kmini.store.domain.QCommunityBoard.communityBoard;
import static com.kmini.store.domain.QUser.user;

@RequiredArgsConstructor
public class CommunityBoardRepositoryImpl implements CommunityBoardRepositoryQsdl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CommunityBoard> findByIdFetchJoin(Long id) {
        CommunityBoard board = queryFactory
                .selectFrom(communityBoard)
                .leftJoin(communityBoard.comments).fetchJoin()
                .join(communityBoard.user, user).fetchJoin()
                .where(communityBoard.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(board);
    }
}
