/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.CRC32;

/**
 * 随机数工具类
 *
 * @author legendshop
 */
public class RandomUtil {

	/**
	 * 得到订单随机数.
	 *
	 * @param userName the user name
	 * @return the order nember
	 */
	public synchronized static String getOrderNumber(String userName) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String orderNumber = simpledateformat.format(new Date());
		Random r = new Random();
		int length = userName.length();
		orderNumber = orderNumber + userName.subSequence(length - 2, length) + randomNumeric(r, 3);
		return orderNumber;
	}

	/**
	 * 生成随机用户名
	 **/
	public static String generateUserName() {
		Random random = new Random();
		long millis = System.currentTimeMillis();
		CRC32 crc32 = new CRC32();
		crc32.update(String.valueOf(millis).getBytes());
		return String.valueOf(crc32.getValue() + random.nextInt(10) + random.nextInt(10));
	}

	/**
	 * 得出随机流水号
	 *
	 * @return
	 */
	public synchronized static String getRandomSn() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String orderNumber = simpledateformat.format(new Date());
		Random r = new Random();
		orderNumber = orderNumber + randomNumeric(r, 5);
		return orderNumber;
	}

	/**
	 * 得出随机流水号
	 *
	 * @return
	 */
	public synchronized static String getRandomSn(Date date, String pattern) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
		String orderNumber = simpledateformat.format(date);
		Random r = new Random();
		orderNumber = orderNumber + randomNumeric(r, 8);
		return orderNumber;
	}


	/**
	 * Random numeric.
	 *
	 * @param random the random
	 * @param count  the count
	 * @return the string
	 */
	public static String randomNumeric(Random random, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			int val = random.nextInt(10);
			sb.append(String.valueOf(val));
		}
		return sb.toString();
	}

	/**
	 * Random.
	 *
	 * @param count the count
	 * @return the int
	 */
	public static int random(int count) {
		Random random = new Random();
		return random.nextInt(count);
	}

	/**
	 * Generate random.
	 *
	 * @return the integer
	 */
	public static Integer generateRandom() {
		Random r = new Random();
		return new Integer(r.nextInt(100000));
	}


	/**
	 * 随机产生的邮箱验证码
	 *
	 * @param length 位数
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}


	/**
	 * 随机产生的6位短信验证码
	 *
	 * @return
	 */
	public static String getRandomSMSCode() {
		//发出随机产生的验证码短信
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		int i = 0;
		while (i < 6) {
			int t = random.nextInt(9);
			stringBuffer.append(t);
			i++;
		}
		return stringBuffer.toString();
	}

	/**
	 * 随机产生的若干位短信验证码
	 *
	 * @param length 位数
	 * @return
	 */
	public static String getRandomSMSCode(int length) {
		return getRandomSMSCode(length);
	}

	/**
	 * 优惠券券号生成器
	 *
	 * @return
	 */
	public synchronized static String getRandomCouponNumberGenerator() {
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String sn = simpledateformat.format(new Date());
		Random r = new Random();
		sn = sn + randomNumeric(r, 5);
		return sn;
	}

	/**
	 * 优惠券卡密生成器
	 *
	 * @return
	 */
	public synchronized static String getRandomCouponPwdGenerator() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 20; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 提货码生成器
	 *
	 * @return
	 */
	public synchronized static String getRandomSinceMentionCodeGenerator() {
		Random random = new Random();
		StringBuffer stringBuffer = new StringBuffer();
		int i = 0;
		while (i < 6) {
			int t = random.nextInt(9);
			stringBuffer.append(t);
			i++;
		}
		return stringBuffer.toString();
	}

}
