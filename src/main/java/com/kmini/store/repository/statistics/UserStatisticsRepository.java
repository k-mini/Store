package com.kmini.store.repository.statistics;

import com.kmini.store.domain.type.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.kmini.store.domain.type.Gender.MAN;
import static com.kmini.store.domain.type.Gender.WOMAN;

@Repository
@RequiredArgsConstructor
public class UserStatisticsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Integer> selectUserJoinStatisticsAnnual(Integer year) {

        String sql =
                "WITH MONTHS AS \n" +
                        "(SELECT '01' joinMonth FROM DUAL \n" +
                        "UNION ALL SELECT '02' joinMonth FROM DUAL \n" +
                        "union all SELECT '03' joinMonth FROM DUAL \n" +
                        "union all SELECT '04' joinMonth FROM DUAL \n" +
                        "union all SELECT '05' joinMonth FROM DUAL \n" +
                        "union all SELECT '06' joinMonth FROM DUAL \n" +
                        "union all SELECT '07' joinMonth FROM DUAL \n" +
                        "union all SELECT '08' joinMonth FROM DUAL \n" +
                        "union all SELECT '09' joinMonth FROM DUAL \n" +
                        "union all SELECT '10' joinMonth FROM DUAL \n" +
                        "union all SELECT '11' joinMonth FROM DUAL \n" +
                        "union all SELECT '12' joinMonth FROM DUAL) \n" +

                        "SELECT   m.joinmonth, \n" +
                        "      IF(uu.joinnumber IS NULL, 0, uu.joinnumber) AS joinNumber \n" +
                        "   FROM  months m \n" +
                        "   LEFT JOIN( \n" +
                        "       SELECT DATE_FORMAT(u.created_date,'%m') AS 'joinmonth', \n" +
                        "               COUNT(u.user_id) AS 'joinnumber' \n" +
                        "       FROM users u \n" +
                        "       where DATE_FORMAT(u.created_date,'%Y') = :year \n" +
                        "       GROUP BY DATE_FORMAT(u.created_date,'%m') \n" +
                        "       ORDER BY joinmonth ASC) uu \n" +
                        "ON m.joinmonth = uu.joinmonth ";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new MonthRowMapper());
    }

    public Map<Gender, Integer> selectUserGenderCntStatisticsAnnual(Integer year) {
        String sql =
                    "WITH TMP AS ( \n" +
                    "SELECT 'M' gender FROM DUAL \n" +
                    "UNION ALL SELECT 'W' gender FROM DUAL)\n" +

                    "SELECT TMP.gender,\n" +
                    "      IF(uu.cnt IS NULL, 0, uu.cnt) AS cnt \n" +
                    " FROM TMP\n" +
                    " LEFT JOIN\n" +
                        " (SELECT gender, \n" +
                                " COUNT(user_id) AS cnt \n" +
                        "  FROM users u \n" +
                        "  WHERE DATE_FORMAT(u.created_date, '%Y') = :year \n" +
                        "  GROUP BY u.gender) uu\n" +
                        "  ON TMP.gender = uu.gender";
        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        List<Integer> genderJoinCntList = jdbcTemplate.query(sql, params, new GenderCntRowMapper());
        return Map.of(MAN, genderJoinCntList.get(0), WOMAN, genderJoinCntList.get(1));
    }

    public List<Integer> selectUserAgeStatisticsAnnual(Integer year) {
        String sql =
                "WITH TMP AS ( \n" +
                    "SELECT 10 generation FROM DUAL \n" +
                    "UNION ALL SELECT 20 generation FROM DUAL \n" +
                    "UNION ALL SELECT 30 generation FROM DUAL \n" +
                    "UNION ALL SELECT 40 generation FROM DUAL \n" +
                    "UNION ALL SELECT 50 generation FROM DUAL \n" +
                    "UNION ALL SELECT 60 generation FROM DUAL \n" +
                    "UNION ALL SELECT 70 generation FROM DUAL) \n" +

                    "SELECT TMP.generation, \n" +
                    "       IF(uu.cnt IS NULL, 0, uu.cnt) AS cnt \n" +
                    "   FROM TMP \n" +
                    "   LEFT JOIN \n" +
                    "       (SELECT \n" +
                    "           TRUNCATE(DATE_FORMAT(NOW(), '%Y') - DATE_FORMAT(birthdate,'%Y'),-1) generation, \n" +
                    "           COUNT(user_id) cnt \n" +
                    "       FROM users u \n" +
                    "       WHERE DATE_FORMAT(u.created_date, '%Y') = :year \n" +
                    "       GROUP BY generation) uu \n" +

                    "   ON TMP.generation = uu.generation \n" +
                    "   ORDER BY tmp.generation ASC";

        MapSqlParameterSource params = new MapSqlParameterSource("year", year);

        return jdbcTemplate.query(sql, params, new GenerationCntRowMapper());
    }

    public static class MonthRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("joinNumber");
        }
    }
    public static class GenderCntRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("cnt");
        }
    }

    public static class GenerationCntRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("cnt");
        }
    }
}
