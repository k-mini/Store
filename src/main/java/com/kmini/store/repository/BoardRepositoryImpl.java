package com.kmini.store.repository;

import com.kmini.store.domain.*;
import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.dto.search.BoardSearchCond;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.kmini.store.domain.QBoard.board;
import static com.kmini.store.domain.QBoardCategory.boardCategory;
import static com.kmini.store.domain.QCategory.category;
import static com.kmini.store.domain.QComment.comment;

public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    // 게시판 페이징
    @Override
    public Page<Board> findByCategories(BoardSearchCond cond, Pageable pageable) {

        CategoryType categoryType = cond.getSubCategoryName();

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
