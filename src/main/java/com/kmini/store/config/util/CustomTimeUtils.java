package com.kmini.store.config.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomTimeUtils {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm");

    public static String convertTime(LocalDateTime localDateTime) {
        return formatter.format(localDateTime);
    }
}
