package com.kmini.store.config.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomTimeUtils {

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm");
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static String convertTime(LocalDateTime localDateTime) {
        return dateTimeFormatter.format(localDateTime);
    }

    public static String convertTime(LocalDate localDate) {
        return dateFormatter.format(localDate);
    }
}
