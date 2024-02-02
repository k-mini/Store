package com.kmini.store.repository;

import com.kmini.store.domain.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

@ActiveProfiles("test")
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    EntityManager em;

//    @TestConfiguration
//    static class TestConfig implements MessageFormattingStrategy {
//        @Bean
//        public JPAQueryFactory queryFactory(EntityManager em) {
//            return new JPAQueryFactory(em);
//        }
//        @PostConstruct
//        public void setLogMessageFormat() {
//            P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
//        }
//
//        @Override
//        public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
//            sql = formatSql(category, sql);
//            return String.format("[%s] | %d ms | %s", category, elapsed, formatSql(category, sql));
//        }
//
//        private String formatSql(String category, String sql) {
//            if (sql != null && !sql.trim().isEmpty() && com.p6spy.engine.logging.Category.STATEMENT.getName().equals(category)) {
//                String trimmedSQL = sql.trim().toLowerCase(Locale.ROOT);
//                if (trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")) {
//                    sql = FormatStyle.DDL.getFormatter().format(sql);
//                } else {
//                    sql = FormatStyle.BASIC.getFormatter().format(sql);
//                }
//                return sql;
//            }
//            return sql;
//        }
//    }

//    @Test
    void findSuperCategories() {

        Category trade = categoryRepository.save(new Category("TRADE", null));
        Category community = categoryRepository.save(new Category("COMMUNITY", null));

        categoryRepository.save(new Category("CLOTHES","의류" ,trade));
        categoryRepository.save(new Category("ELECTRONICS", "전자",trade));
        categoryRepository.save(new Category("FREE", "자유", community));
        categoryRepository.save(new Category("QNA", "질문" , community));
        em.clear();
        System.out.println(" 초기화 완료=================");

    }
}