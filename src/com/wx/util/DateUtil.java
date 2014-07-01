package com.wx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
	private DateUtil() {
	}

	/**
	 * 当前时间年份
	 * 
	 * @return
	 */
	public static String getYearByNow() {

		return formatDate("yyyy");
	}

	public static String getYear(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return formatDate("yyyy", simpleDateFormat.parse(date));
	}

	/**
	 * 当前时间月份，月份格式为M，既 1月返回1而不是01
	 * 
	 * @return
	 */
	public static String getMonthByNow() {

		return formatDate("M");
	}

	public static String getMonth(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return formatDate("M", simpleDateFormat.parse(date));
	}

	/**
	 * 当前时间几号，号格式为d，既 1号返回1而不是01
	 * 
	 * @return
	 */
	public static String getDayByNow() {

		return formatDate("d");
	}

	public static String getDay(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return formatDate("d", simpleDateFormat.parse(date));
	}

	public static String formatDate(String pattern, Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static String formatDate(String pattern) {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static void main(String[] args) throws ParseException {
		String string = "20141101";
		System.out.println(getYear(string));
		System.out.println(getMonth(string));
		System.out.println(getDay(string));
	}

}
