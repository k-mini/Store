package com.kmini.store.service;

import com.kmini.store.config.QuerydslConfig;
import com.kmini.store.domain.ItemBoard;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.repository.board.ItemBoardRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@DataJpaTest
class ItemBoardServiceImplTest {

    @Autowired
    private ItemBoardRepository itemBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void test() {
        userRepository.save(new User("test","1234","test@gmail.com", UserRole.USER, UserStatus.SIGNUP,null));
        em.clear();
        User user = userRepository.getReferenceById(1L);
        itemBoardRepository.save(new ItemBoard(user, "Test", "Test"));
        ItemBoard itemBoard = itemBoardRepository.getReferenceById(156L);
        System.out.println("====================");
    }

    @TestConfiguration
    static class testConfig {
        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager em) {
            return new JPAQueryFactory(em);
        }
    }
}