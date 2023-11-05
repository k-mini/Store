package com.kmini.store.repository.board;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.search.BoardSearchCond;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.kmini.store.domain.QBoard.board;
import static com.kmini.store.domain.QBoardCategory.boardCategory;
import static com.kmini.store.domain.QCategory.category;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    // 게시판 페이징
    @Override
    public Page<Board> findByCategories(BoardSearchCond cond, Pageable pageable) {

        CategoryType categoryType = cond.getSubCategoryName();
        // order 지시자 만들기
        OrderSpecifier<?>[] orderSpecifiers = CustomPageUtils.getOrderSpecifiers(pageable, board);
        // 검색 조건 만들기
        

        List<Board> content = queryFactory
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

                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers)
                .fetch();

        int size = queryFactory
                .select(boardCategory.count())
                .from(boardCategory)
                .where(boardCategory.category
                        .eq(
                                JPAExpressions.selectFrom(category)
                                        .where(category.categoryType.eq(categoryType))
                        )
                ).fetchOne().intValue();

//        return null;
        return new PageImpl<>(content,pageable,size);
    }
}
