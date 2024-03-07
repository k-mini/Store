package com.kmini.store.repository.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardStatisticsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Integer> selectBoardWriteStatisticsAnnual(Integer year) {

        String sql =
                "WITH TMP AS (SELECT '01' months FROM DUAL \n" +
                        "UNION ALL SELECT '02' months FROM DUAL\n" +
                        "union all SELECT '03' months FROM DUAL\n" +
                        "union all SELECT '04' months FROM DUAL\n" +
                        "union all SELECT '05' months FROM DUAL\n" +
                        "union all SELECT '06' months FROM DUAL\n" +
                        "union all SELECT '07' months FROM DUAL\n" +
                        "union all SELECT '08' months FROM DUAL\n" +
                        "union all SELECT '09' months FROM DUAL\n" +
                        "union all SELECT '10' months FROM DUAL\n" +
                        "union all SELECT '11' months FROM DUAL\n" +
                        "union all SELECT '12' months FROM DUAL)\n" +

                        "SELECT m.months,\n" +
                        "       if(bb.cnt IS NULL, 0, bb.cnt) AS 'cnt'\n" +
                        "  FROM TMP m \n" +
                        "  LEFT JOIN( \n" +
                        "       SELECT \n" +
                        "           DATE_FORMAT(b.created_date,'%m') AS 'months', \n" +
                        "           COUNT(b.board_id) AS 'cnt' \n" +
                        "         FROM board b \n" +
                        "        WHERE DATE_FORMAT(b.created_date,'%Y') = :year \n" +
                        "        GROUP BY months) bb \n" +
                        "  ON m.months = bb.months";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new MonthRowMapper());
//        return List.of(2, 13, 2, 11, 10, 5, 25, 17, 9, 5, 14, 23);
    }

    public List<StatisticsCategoryRow> selectBoardCategoryStatisticsAnnual(Integer year) {

        String sql =
                "WITH TMP AS (SELECT category_id, category_ko_name, category_name \n" +
                        "       FROM category c WHERE c.super_category IS NOT null) \n" +

                        "SELECT tmp.category_id, \n" +
                        "       tmp.category_ko_name, \n" +
                        "       tmp.category_name, \n" +
                        "       IF(tmp2.cnt IS NULL, 0, tmp2.cnt) cnt \n" +
                        "  FROM tmp \n" +
                        "   LEFT JOIN ( \n" +
                        "      SELECT bc.category_id,\n" +
                        "             COUNT(bc.category_id) AS cnt\n" +
                        "        FROM board b\n" +
                        "        JOIN board_category bc\n" +
                        "          ON b.board_id = bc.board_id\n" +
                        "       WHERE DATE_FORMAT(b.created_date, '%Y') = :year \n" +
                        "         AND bc.category_id IN ( " +
                        "                                 SELECT ca.category_id " +
                        "                                   FROM category ca " +
                        "                                  WHERE ca.super_category IS NOT NULL) \n" +
                        "       GROUP BY bc.category_id) tmp2 \n" +
                        "  ON tmp.category_id = tmp2.category_id";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new CategoryRowMapper());
//        return List.of(56, 170, 245, 120, 98, 49, 10);
    }

    public static class MonthRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("cnt");
        }
    }

    public static class CategoryRowMapper implements RowMapper<StatisticsCategoryRow> {
        @Override
        public StatisticsCategoryRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StatisticsCategoryRow(rs.getLong("category_id"), rs.getString("category_ko_name"),
                    rs.getString("category_name"), rs.getInt("cnt"));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class StatisticsCategoryRow {
        private long categoryId;
        private String categoryKoName;
        private String categoryName;
        private int cnt;
    }
}
