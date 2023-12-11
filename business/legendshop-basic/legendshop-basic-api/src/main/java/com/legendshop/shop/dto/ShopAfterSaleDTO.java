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
import java.util.Date;

/**
 * 售后服务说明表(ShopAfterSale)实体类
 *
 * @author legendshop
 */
@Data
public class ShopAfterSaleDTO implements Serializable {


	private static final long serialVersionUID = -7830315626660159439L;
	/**
	 * ID
	 */
	private Long id;


	/**
	 * 用户名称
	 */
	private Long userId;


	/**
	 * shopId
	 */
	private Long shopId;


	/**
	 * 标题
	 */
	private String title;


	/**
	 * 内容
	 */
	private String content;


	/**
	 * 录入时间
	 */
	private Date recDate;

}
