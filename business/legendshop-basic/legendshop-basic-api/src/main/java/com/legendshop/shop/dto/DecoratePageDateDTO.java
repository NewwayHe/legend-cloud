/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 装修数据DTO
 *
 * @author legendshop
 * @Author lengendshop
 */
@Data
public class DecoratePageDateDTO implements Serializable {

	/**
	 * 主题颜色
	 */
	private String themeColor;

	/**
	 * 来源
	 */
	private String platform;

	/**
	 * 背景颜色
	 */
	private String backgroundColor;

	/**
	 * 是否开启自定义装修
	 */
	private Boolean enable;
}
