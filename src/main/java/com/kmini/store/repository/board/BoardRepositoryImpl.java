package com.kmini.store.repository.board;

import com.kmini.store.config.util.CustomPageUtils;
import com.kmini.store.domain.Board;
import com.kmini.store.dto.request.SearchDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

import static com.kmini.store.domain.QBoard.board;
import static com.kmini.store.domain.QBoardCategory.boardCategory;
import static com.kmini.store.domain.QCategory.category;
import static com.kmini.store.domain.QUser.user;

@RequiredArgsConstructor
@Slf4j
public class BoardRepositoryImpl implements BoardRepositoryQsdl {

    private final JPAQueryFactory queryFactory;

    // 게시판 페이징
    @Override
    public Page<Board> findBoards(BoardSearchCond cond, Pageable pageable) {

        String categoryName = cond.getCategoryName();
        String subCategoryName = cond.getSubCategoryName();

        // order 지시자 만들기
        OrderSpecifier<?>[] orderSpecifiers = CustomPageUtils.getOrderSpecifiers(pageable, board);

        JPAQuery<Board> query = queryFactory
                .selectFrom(board)
                .where(
                        board.in(
                                JPAExpressions.select(boardCategory.board)
                                        .from(boardCategory)
                                        .where(boardCategory.category.eq(
                                                        subCategoryName.equals("all") ?
                                                                JPAExpressions.selectFrom(category)
                                                                        .where(category.categoryName.eq(categoryName)) :
                                                                JPAExpressions.selectFrom(category)
                                                                        .where(category.categoryName.eq(subCategoryName))
                                                )
                                        )
                        ),
                        searchTypeLike(cond.getSearchType(),cond.getSearchKeyword())
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
                                                subCategoryName.equals("all") ?
                                                        JPAExpressions.selectFrom(category)
                                                                .where(category.categoryName.eq(categoryName)) :
                                                        JPAExpressions.selectFrom(category)
                                                                .where(category.categoryName.eq(subCategoryName))
                                                )
                                        )
                        ),
                        searchTypeLike(cond.getSearchType(),cond.getSearchKeyword())
                );

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchOne);
    }

    private BooleanExpression searchTypeLike(String searchType, String searchKeyword) {
        if (searchType == null || searchKeyword == null) {
            return null;
        }

        try {
            Field searchTypeField = Class.forName("com.kmini.store.domain.QBoard")
                    .getDeclaredField(searchType);
            StringPath typePath = (StringPath) searchTypeField.get(board);
            return StringUtils.hasText(searchType) ? typePath.like("%" + searchKeyword + "%") : null;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("해당하는 변수명이 없습니다. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("해당 클래스를 찾을 수 없습니다. ", e);
        }
    }

    @Data
    public static class BoardSearchCond {

        private String categoryName;

        private String subCategoryName;

        // 제목
        private String title;
        // 내용
        private String content;
        // 검색 타입
        private String searchType;
        private String searchKeyword;

        public BoardSearchCond(String categoryName, String subCategoryName, SearchDto.SearchBoardDto searchBoardDto) {
            this.categoryName = categoryName;
            this.subCategoryName = subCategoryName;
//            this.title = "title".equals(searchBoardDto.getSearchType()) ? searchBoardDto.getSearchKeyword() : null;
//            this.content = "content".equals(searchBoardDto.getSearchType()) ? searchBoardDto.getSearchKeyword() : null;
            this.searchType = searchBoardDto.getSearchType();
            this.searchKeyword = searchBoardDto.getSearchKeyword();
        }
    }
}
