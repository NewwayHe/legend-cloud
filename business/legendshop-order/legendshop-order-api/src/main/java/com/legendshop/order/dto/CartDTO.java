/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车DTO
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO implements Serializable {

	private static final long serialVersionUID = 920053626291416366L;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 商品id
	 */
	private Long productId;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * 购物车产品个数
	 */
	private Integer totalCount;


	/**
	 * 选中状态 1:选中 0：未选中
	 */
	private Boolean selectFlag = Boolean.TRUE;

	/**
	 * 店铺选中状态 1:选中 0：未选中
	 */
	private Boolean shopSelectFlag;

	/**
	 * 活动ID
	 */
	private Long marketingId = 0L;

}
