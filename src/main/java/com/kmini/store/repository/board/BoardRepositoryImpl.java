package com.kmini.store.repository.board;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.domain.Board;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.search.BoardSearchCond;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.kmini.store.domain.QBoard.board;
import static com.kmini.store.domain.QBoardCategory.boardCategory;
import static com.kmini.store.domain.QCategory.category;
import static com.kmini.store.domain.QUser.user;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 게시판 페이징
    @Override
    public Page<Board> findBoards(BoardSearchCond cond, Pageable pageable) {

        CategoryType categoryType = cond.getCategoryType();
        // order 지시자 만들기
        OrderSpecifier<?>[] orderSpecifiers = CustomPageUtils.getOrderSpecifiers(pageable, board);

        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .where(
                        board.in(
                                JPAExpressions.select(boardCategory.board)
                                        .from(boardCategory)
                                        .where(boardCategory.category.eq(
                                                        JPAExpressions.selectFrom(category)
                                                                .where(category.categoryType.eq(categoryType))
                                                )
                                        )
                        ),
                        titleLike(cond.getTitle()),
                        contentLike(cond.getContent())
                )
                .join(board.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers);

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.in(
                                JPAExpressions.select(boardCategory.board)
                                        .from(boardCategory)
                                        .where(boardCategory.category.eq(
                                                        JPAExpressions.selectFrom(category)
                                                                .where(category.categoryType.eq(categoryType))
                                                )
                                        )
                        ),
                        titleLike(cond.getTitle()),
                        contentLike(cond.getContent())
                );

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleLike(String title) {
        return StringUtils.hasText(title) ? board.title.like("%" + title + "%") : null;
    }

    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? board.content.like("%" + content + "%") : null;
    }
}
