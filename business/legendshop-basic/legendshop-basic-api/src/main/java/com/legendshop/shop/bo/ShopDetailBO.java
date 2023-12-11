/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Schema(description = "商家用户详细信息BO")
@Data
public class ShopDetailBO implements Serializable {

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	private String shopName;


	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Schema(description = "店铺类型0.专营店1.旗舰店2.自营店")
	private Integer shopType;

	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;

	/**
	 * 物流评分
	 */
	@Schema(description = "物流评分")
	private BigDecimal dvyTypeAvg;

	/**
	 * 服务评分
	 */
	@Schema(description = "服务评分")
	private BigDecimal shopCommAvg;

	/**
	 * 描述评分
	 */
	@Schema(description = "描述评分")
	private BigDecimal productCommentAvg;

	/**
	 * 综合评分
	 */
	@Schema(description = "综合评分")
	private BigDecimal score;

	@Schema(description = "是否已被收藏")
	private Boolean collectionFlag;


	/**
	 * 店铺入驻类型  2：供应商  3:分销商   {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "店铺入驻类型  2：供应商  3:分销商")
	private Integer applyForType;

	public ShopDetailBO() {
		this.dvyTypeAvg = new BigDecimal("0.00");
		this.shopCommAvg = new BigDecimal("0.00");
		this.productCommentAvg = new BigDecimal("0.00");
	}
}
