package com.kmini.store.domain.admin.statistics.comment.repository;

import com.kmini.store.global.constants.Gender;
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
import java.util.Map;

import static com.kmini.store.global.constants.Gender.MAN;
import static com.kmini.store.global.constants.Gender.WOMAN;

@Repository
@RequiredArgsConstructor
public class CommentStatisticsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Integer> selectCommentWriteStatisticsAnnual(Integer year) {

        String sql =
                "WITH MONTHS AS (\n" +
                        "SELECT '01' writeMonth FROM DUAL \n" +
                        "UNION ALL SELECT '02' writeMonth FROM DUAL\n" +
                        "union all SELECT '03' writeMonth FROM DUAL\n" +
                        "union all SELECT '04' writeMonth FROM DUAL\n" +
                        "union all SELECT '05' writeMonth FROM DUAL\n" +
                        "union all SELECT '06' writeMonth FROM DUAL\n" +
                        "union all SELECT '07' writeMonth FROM DUAL\n" +
                        "union all SELECT '08' writeMonth FROM DUAL\n" +
                        "union all SELECT '09' writeMonth FROM DUAL\n" +
                        "union all SELECT '10' writeMonth FROM DUAL\n" +
                        "union all SELECT '11' writeMonth FROM DUAL\n" +
                        "union all SELECT '12' writeMonth FROM DUAL)\n" +

                        "SELECT m.writeMonth,\n" +
                        "        IF(cc.cnt IS NULL, 0, cc.cnt) AS cnt\n" +
                        "  FROM  months m\n" +
                        "  LEFT JOIN \n" +
                        "       (SELECT \n" +
                        "           DATE_FORMAT(c.created_date,'%m') AS 'writeMonth',\n" +
                        "           COUNT(c.comment_id) AS 'cnt'\n" +
                                "FROM comment c\n" +
                                "where DATE_FORMAT(c.created_date,'%Y') = :year \n" +
                                "GROUP BY writeMonth\n" +
                                "ORDER BY writeMonth ASC) cc\n" +
                        "   ON m.writeMonth = cc.writeMonth";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new MonthRowMapper());
//        return List.of(2, 13, 2, 11, 10, 5, 25, 17, 9, 5, 14, 23);
    }

    public List<StatisticsCategoryRow> selectCommentCategoryStatisticsAnnual(Integer year) {

        String sql =
                "WITH TMP AS ( \n" +
                    " SELECT category_id," +
                        "    category_ko_name," +
                        "    category_name \n" +
                    "   FROM category c WHERE c.super_category IS NOT null)\n" +

                    "SELECT tmp.category_id,\n" +
                    "       tmp.category_ko_name,\n" +
                    "       tmp.category_name,\n" +
                    "       IF(tmp2.cnt IS NULL, 0, tmp2.cnt) cnt\n" +
                    "  FROM tmp\n" +
                    "  LEFT join\n" +
                    "       (SELECT bc.category_id,\n" +
                    "               COUNT(c.comment_id) AS CNT\n" +
                        "      FROM comment c\n" +
                        "      JOIN board b\n" +
                        "        ON b.board_id = c.board_id\n" +
                        "      JOIN board_category bc\n" +
                        "        ON b.board_id = bc.board_id\n" +
                        "     WHERE DATE_FORMAT(c.created_date, '%Y') = :year \n" +
                        "       AND bc.category_id IN ( SELECT zz.category_id \n" +
                    "                                     FROM category zz \n" +
                    "                                    WHERE zz.super_category IS not NULL) \n" +
                    "     GROUP BY bc.category_id) tmp2\n" +
                    "   ON tmp.category_id = tmp2.category_id";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new CategoryRowMapper());
//        return List.of(30,10,25,35);
    }

    public Map<Gender, Integer> selectCommentGenderStatisticsAnnual(Integer year) {

        String sql =
                "WITH TMP AS \n" +
                        "(SELECT 'M' gender FROM DUAL \n" +
                        " UNION ALL SELECT 'W' gender FROM DUAL) \n" +

                        "SELECT tmp.gender,\n " +
                        "       IF(cc.cnt IS NULL, 0, cc.cnt) AS cnt \n" +
                        "  FROM tmp \n" +
                        "  LEFT JOIN \n" +
                        "  (SELECT  u.gender AS gender, \n" +
                        "           COUNT(c.comment_id) AS cnt\n" +
                        "     FROM comment c \n" +
                        "     JOIN users u \n" +
                        "       ON u.user_id = c.user_id \n" +
                        "    WHERE DATE_FORMAT(c.created_date, '%Y') = :year \n" +
                        "    GROUP BY u.gender) cc \n" +
                        "ON tmp.gender = cc.gender \n" +
                        "ORDER BY gender";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        List<Integer> genderCommentList = jdbcTemplate.query(sql, params, new GenderRowMapper());
        return Map.of(MAN, genderCommentList.get(0), WOMAN, genderCommentList.get(1));
//        return List.of(55, 45);
    }

    public static class MonthRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("cnt");
        }
    }

    public static class GenderRowMapper implements RowMapper<Integer> {
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
