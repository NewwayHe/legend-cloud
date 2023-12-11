/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

/**
 * 用户名称转换
 *
 * @author legendshop
 */
public interface UserLoginDao {

	/**
	 * 根据用户输入，把可能的邮件或者手机号码找到对应的用户名称.
	 *
	 * @param name the name
	 * @return the string
	 */
	String convertUserLoginName(String name);


	/**
	 * 根据商家输入，把可能的邮件或者手机号码找到对应的用户名称.
	 *
	 * @param name the name
	 * @return the string
	 */
	String convertShopUserLoginName(String name);
}
