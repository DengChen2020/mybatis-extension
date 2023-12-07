package io.github.dengchen2020.mybatis.extension.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author dengchen
 */
public class DateUtils {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String parse(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String parse(Date date) {
        return localDateTime(date).format(dateTimeFormatter);
    }

    public static Date date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime localDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

}
