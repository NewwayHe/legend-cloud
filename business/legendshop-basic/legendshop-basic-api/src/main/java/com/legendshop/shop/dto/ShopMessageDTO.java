/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家信息DTO")
public class ShopMessageDTO extends BaseDTO implements Serializable {

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 二维码
	 */
	@Schema(description = "二维码")
	private String qrCode;

	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;

	/**
	 * 店铺地址
	 */
	@Schema(description = "店铺地址")
	private String shopAddress;


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

	/**
	 * 主体申请类型，0，个人。  1，商家
	 * {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "主体申请类型.1:个人、2:商家、3:分销员")
	private Integer applyForType;
}
