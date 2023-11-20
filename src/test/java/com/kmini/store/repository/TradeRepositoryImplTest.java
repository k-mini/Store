package com.kmini.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;

@DataJpaTest
class TradeRepositoryImplTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void getLatestTrade() {
//        tradeRepository.getLatestTrade(1L, PageRequest.of(0,1));
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }

}