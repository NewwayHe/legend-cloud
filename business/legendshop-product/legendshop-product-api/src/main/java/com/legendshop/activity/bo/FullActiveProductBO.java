/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 包邮的商品
 *
 * @author legendshop
 */
@Data
public class FullActiveProductBO implements Serializable {

	private static final long serialVersionUID = 4905799483045740864L;

	/**
	 * 商品ID
	 */
	private Long productId;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品图片
	 */
	private String pic;

	/**
	 * 商品实际库存
	 */
	private Integer actualStocks;

	/**
	 * 商品销售价格
	 */
	private Double price;

	/**
	 * 包邮活动表的ID，如果为空，证明没有参加包邮活动
	 */
	private Long activeId;

	/**
	 * 编辑自己的已选择
	 */
	private Integer hook;
}
