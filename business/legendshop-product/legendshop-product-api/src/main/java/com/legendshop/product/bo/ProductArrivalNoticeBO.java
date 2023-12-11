/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品到货通知表BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理-平台类目关联DTO")
public class ProductArrivalNoticeBO implements Serializable {


	private static final long serialVersionUID = 3479065670691912847L;

	/**
	 * 到货通知记录表ID
	 */
	@Schema(description = "到货通知记录表ID")
	private Long id;


	/**
	 * 消费者用户id
	 */
	@Schema(description = "消费者用户id")
	private Long userId;

	/**
	 * 消费者用户名称
	 */
	@Schema(description = "消费者用户名称")
	private String nickName;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;


	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long productId;


	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;


	/**
	 * 手机
	 */
	@Schema(description = "手机")
	private String mobilePhone;


	/**
	 * 状态 0:尚未通知用户 1:已通知用户
	 */
	@Schema(description = "状态 0:尚未通知用户 1:已通知用户")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

}
