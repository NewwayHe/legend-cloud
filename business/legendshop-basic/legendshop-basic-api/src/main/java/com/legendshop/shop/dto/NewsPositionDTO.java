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
 * 新闻位置表(NewsPosition)实体类
 *
 * @author legendshop
 */
@Data
public class NewsPositionDTO implements Serializable {


	private static final long serialVersionUID = -254507614416907441L;
	/**
	 * 主键id
	 */
	private Long id;


	/**
	 * 新闻Id
	 */
	private Long news;


	/**
	 * 新闻位置[底部、头部、...]
	 */
	private Integer position;


	/**
	 * 排序
	 */
	private Integer seq;

}
