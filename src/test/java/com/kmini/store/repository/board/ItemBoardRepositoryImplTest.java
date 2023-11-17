package com.kmini.store.repository.board;

import com.kmini.store.dto.response.ItemBoardDto;
import com.kmini.store.dto.response.ItemBoardDto.ItemBoardRespDetailDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemBoardRepositoryImplTest {

    @Autowired
    ItemBoardRepository itemBoardRepository;

    @Test
    void findDetailById() {

        ItemBoardRespDetailDto result = itemBoardRepository.findDetailById(1L).get();
        System.out.println("result = " + result);
    }

    @TestConfiguration
    static class testConfig {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }
}