package com.xwy.common.utils;

import java.util.Random;

/**
 * 随机数工具类
 * 
 * @author xiangwy
 * @date: 2020-12-02 09:33:00
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
public final class RandomUtil {

	/**
	 * 生成随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getRsaKey(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@#$%^&*!0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 生成随机数，包括a-z
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/** 生成随机数,0-9 */
	public static String getRandom(int length) { // length表示生成字符串的长度
		StringBuilder sizesb = new StringBuilder("1");
		for (int i = 1; i < length; i++) {
			sizesb.append("0");
		}
		return String.valueOf((long) ((Math.random() * length + 1) * Long.parseLong(sizesb.toString())));
	}

	/**
	 * 获取6位随机数
	 */
	public static String getRandom6Num() {
		return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
	}

	/**
	 * 获取6位随机数+系统时间戳
	 */
	public static String getRandomSystemTimeMillis() {
		return getRandom6Num() + System.currentTimeMillis();
	}
}
