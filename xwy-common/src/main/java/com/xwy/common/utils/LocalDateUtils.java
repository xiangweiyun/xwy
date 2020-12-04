package com.xwy.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:40:43
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public class LocalDateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static String YYYY = "yyyy";

	public static String YYYY_MM = "yyyy-MM";

	public static String YYYY_MM_DD = "yyyy-MM-dd";

	public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate(LocalDateTime data) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(data);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(LocalDateTime.now());
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(LocalDateTime date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = date.format(DateTimeFormatter.ofPattern(pattern[0].toString()));
		} else {
			formatDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(LocalDateTime date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(LocalDateTime.now(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(LocalDateTime.now(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(LocalDateTime.now(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(LocalDateTime.now(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(LocalDateTime.now(), "E");
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 和当前时间比较(只比较到天数)
	 * 
	 * @param beforeStr
	 * @param afterStr
	 * @param afterSub  加天数
	 * @return
	 */
	public static Boolean compareToNow(String beforeStr, String afterStr, Integer afterSub) {
		int now = Integer.parseInt(getDate().replace("-", ""));
		int before = Integer.parseInt(beforeStr.replace("-", ""));
		int after = Integer.parseInt(afterStr.replace("-", "")) - afterSub;
		if (now >= before && now <= after) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return
	 */
	public static LocalTime getLocalTime() {
		return LocalTime.now();
	}

	/**
	 * 获取当前系统日期
	 * 
	 * @return
	 */
	public static LocalDate getLocalDate() {
		return LocalDate.now();
	}

	/**
	 * 获取当前系统日期时间
	 * 
	 * @return
	 */
	public static LocalDateTime getLocalDateTime() {
		return LocalDateTime.now();
	}

	/**
	 * 获取当前系统日期时间字符串
	 * 
	 * @param pattern 自定义格式
	 * @return
	 */
	public static String getLocalDateTimeString(String pattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符串转LocalTime
	 * 
	 * @param time
	 * @param pattern 自定义格式
	 * @return
	 */
	public static LocalTime string2LocalTime(String time, String pattern) {
		return LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符串转LocalDate
	 * 
	 * @param date
	 * @param pattern 自定义格式
	 * @return
	 */
	public static LocalDate string2LocalDate(String date, String pattern) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符串转LocalDateTime
	 * 
	 * @param dateTime
	 * @param pattern  自定义格式
	 * @return
	 */
	public static LocalDateTime string2LocalDateTime(String dateTime, String pattern) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * Date转LocalDateTime
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		Instant instant = date.toInstant();// An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		ZoneId zoneId = ZoneId.systemDefault();// A time-zone ID, such as {@code Europe/Paris}.(时区)
		LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
		return localDateTime;
	}

	/**
	 * Date转LocalDate
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDate date2LocalDate(Date date) {
		Instant instant = date.toInstant();// An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		ZoneId zoneId = ZoneId.systemDefault();// A time-zone ID, such as {@code Europe/Paris}.(时区)
		LocalDate localDate = instant.atZone(zoneId).toLocalDate();
		return localDate;
	}

	/**
	 * Date转LocalDate
	 * 
	 * @param date
	 * @return
	 */
	public static LocalTime date2LocalTime(Date date) {
		Instant instant = date.toInstant();// An instantaneous point on the time-line.(时间线上的一个瞬时点。)
		ZoneId zoneId = ZoneId.systemDefault();// A time-zone ID, such as {@code Europe/Paris}.(时区)
		LocalTime localTime = instant.atZone(zoneId).toLocalTime();
		return localTime;
	}

	/**
	 * LocalDateTime转换为Date
	 * 
	 * @param localDateTime
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);// Combines this date-time with a time-zone to create a
															// ZonedDateTime.
		Date date = Date.from(zdt.toInstant());
		return date;
	}

	public static final String dateTimeNow() {
		return dateTimeNow(YYYYMMDDHHMMSS);
	}

	public static final String dateTimeNow(final String format) {
		return parseDateToStr(format, new Date());
	}

	public static final String parseDateToStr(final String format, final Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(getDay());
	}

}
