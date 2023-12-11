/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

/**
 * 业务配置服务
 *
 * @author legendshop
 */
public interface BusinessSettingService {

	/**
	 * 根据类型获取配置值
	 *
	 * @param type
	 * @return
	 */
	String getByType(String type);


	/**
	 * 根据类型更新配置值
	 *
	 * @param type
	 * @param value
	 * @return
	 */
	boolean updateByType(String type, String value);


}



