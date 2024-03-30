package com.kmini.store.global.util;

import com.kmini.store.global.constants.Gender;

import java.util.List;
import java.util.Map;

import static com.kmini.store.global.constants.Gender.MAN;
import static com.kmini.store.global.constants.Gender.WOMAN;

public class CustomStatisticsUtils {

    public static List<Integer> calculateGenderRatio(Map<Gender, Integer> genderMap) {

        int manCnt = genderMap.get(MAN);
        int womanCnt = genderMap.get(WOMAN);
        double totalCnt = manCnt + womanCnt;

        if (totalCnt == 0) {
            List.of(0,0);
        }
        int manPercent = (int) Math.round((double) manCnt / totalCnt * 100);
        int womanPercent = 100 - manPercent;

        return List.of(manPercent, womanPercent);
    }
}
