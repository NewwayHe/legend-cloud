/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品到货通知表DTO
 *
 * @author legendshop
 */
@Data
public class ProductArrivalNoticeDTO implements Serializable {

	private static final long serialVersionUID = -84051347946566035L;

	/**
	 * 到货通知记录表ID
	 */
	private Long id;


	/**
	 * 消费者用户id
	 */
	private Long userId;


	/**
	 * 商家id
	 */
	private Long shopId;


	/**
	 * 商品id
	 */
	private Long productId;


	/**
	 * skuId
	 */
	@NotNull(message = "skuId不能为空")
	private Long skuId;


	/**
	 * 手机
	 */
	private String mobilePhone;


	/**
	 * 状态 0:尚未通知用户 1:已通知用户
	 */
	private Integer status;


	/**
	 * 创建时间
	 */
	private Date createTime;

}
