package com.tencent.wxcloudrun.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * LocalDateTime 转化工具
 *
 * @author dongdongxie
 * @date 2022/12/12
 */
public class CoreDateUtils {
    private static final Logger logger = LoggerFactory.getLogger(CoreDateUtils.class.getName());

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String TIMESTAMP = "yyyyMMddHHmmss";

    public static String formatDate(Date date) {
        return formatDate(date, DATE);
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, DATETIME);
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, pattern, Locale.CHINA);
    }

    public static String formatDate(String dateStr, String srcPattern, String desPattern) {
        Date date = parseDate(dateStr, srcPattern);
        if (date == null) {
            return null;
        }
        return formatDate(date, desPattern);
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DATE);
    }

    public static Date parseDateTime(String dateStr) {
        return parseDate(dateStr, new String[]{
                DATETIME,
                "yyyy-MM-dd HH:mm:ss.SSS",
        });
    }

    public static Date parseDate(long timeMillis) {
        return Date.from(Instant.ofEpochMilli(timeMillis));
    }

    public static Date parseDate(String dateStr, String pattern) {
        return parseDate(StringUtils.trim(dateStr), new String[]{pattern});
    }

    public static Date parseDate(String dateStr, String[] patterns) {
        if (dateStr == null) {
            return null;
        }
        try {
            return DateUtils.parseDateStrictly(dateStr, patterns);
        } catch (ParseException e) {
            logger.error("日期转换错误, dateStr={}, pattern={}", dateStr, StringUtils.join(patterns, ","));
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static LocalDateTime parseLocalDateTime(String datetimeStr) {
        return CoreDateUtils.parseLocalDateTime(datetimeStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime parseLocalDateTime(String datetimeStr, String pattern) {
        if (datetimeStr == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(datetimeStr, formatter);
        } catch (Exception e) {
            logger.error("本地时间转换错误, datetimeStr={}, pattern={}", datetimeStr, pattern);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String formatLocalDateTime(LocalDateTime datetime) {
        return CoreDateUtils.formatLocalDateTime(datetime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
        if (datetime == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return datetime.format(formatter);
        } catch (Exception e) {
            logger.error("本地时间格式化错误, dateStr={}, pattern={}", datetime, pattern);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static LocalDate parseLocalDate(String dateStr) {
        return CoreDateUtils.parseLocalDate(dateStr, "yyyy-MM-dd");
    }

    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        if (dateStr == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            logger.error("本地时间转换错误, dateStr={}, pattern={}", dateStr, pattern);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String formatLocalDate(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return date.format(formatter);
        } catch (Exception e) {
            logger.error("本地时间格式化错误, dateStr={}, pattern={}", date, pattern);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static boolean test(String dateStr, String pattern) {
        return test(dateStr, new String[]{pattern});
    }

    public static boolean test(String dateStr, String[] patterns) {
        if (dateStr == null) {
            return false;
        }
        try {
            DateUtils.parseDateStrictly(dateStr, patterns);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date convertTo(LocalDateTime localDateTime) {
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime convertFrom(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 计算周岁年龄
     *
     * @param birthday 生日日期
     * @return 年龄
     */
    public static int calculateAge(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        Calendar birthdayCalendar = Calendar.getInstance();
        birthdayCalendar.setTime(birthday);
        int birthYear = birthdayCalendar.get(Calendar.YEAR);

        int diffYears = currentYear - birthYear;
        if (diffYears < 0) {
            throw new RuntimeException("输入生日日期晚于当前时间");
        }
        birthdayCalendar.add(Calendar.YEAR, diffYears);

        if (birthdayCalendar.after(calendar)) {
            // 加上年份后在当前时间之后, 说明未满周岁
            return diffYears - 1;
        }

        return diffYears;
    }

    /**
     * 是否在2个时间段范围内
     *
     * @param timeStr 时间段 格式: 23:00:00-06:00:00
     * @param date    待比较时间
     * @return boolean
     */
    public static boolean isBetweenTime(String timeStr, LocalDateTime date) {
        String[] startEnd = StringUtils.split(timeStr, "-");

        LocalTime start = LocalTime.parse(startEnd[0], DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime end = LocalTime.parse(startEnd[1], DateTimeFormatter.ofPattern("HH:mm:ss"));

        return date.toLocalTime().isAfter(start) && date.toLocalTime().isBefore(end.plusSeconds(1));
    }

    private static String[] chineseWeekDateName = new String[]{"日", "一", "二", "三", "四", "五", "六"};

    /**
     * 格式化显示中文的周几
     *
     * @param date 日期
     * @return 周几
     */
    public static String formatChineseWeekDateName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int weekDate = calendar.get(Calendar.DAY_OF_WEEK);

        return "周" + chineseWeekDateName[weekDate - 1];
    }
}
