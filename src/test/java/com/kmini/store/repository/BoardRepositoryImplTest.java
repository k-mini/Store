package com.kmini.store.repository;

import com.kmini.store.domain.type.CategoryType;
import com.kmini.store.repository.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
class BoardRepositoryImplTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private EntityManager em;

    @Test
    void findByCategoriesTest(){

        boardRepository.findByCategories(null,null);
    }

    @Test
    void jpqlTest() {
        em.createQuery("select count(boardCategory) " +
                        "from com.kmini.store.domain.BoardCategory boardCategory " +
                        "where boardCategory.category = (" +
                "               select category " +
                "                 from com.kmini.store.domain.Category category " +
                                "where category.categoryType = ?1)" )
                .setParameter(1, CategoryType.ELECTRONICS)
                .getFirstResult();
    }

}