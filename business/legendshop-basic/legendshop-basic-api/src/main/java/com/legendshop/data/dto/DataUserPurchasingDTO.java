/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (DataUserPurchasing)DTO
 *
 * @author legendshop
 * @since 2021-03-23 15:42:17
 */
@Data
@Schema(description = "DTO")
public class DataUserPurchasingDTO implements Serializable {

	private static final long serialVersionUID = -74125902503059420L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String mobile;

	/**
	 * 下单金额
	 */
	@Schema(description = "下单金额")
	private BigDecimal totalAmount;

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	private BigDecimal payAmount;

	/**
	 * 成交金额
	 */
	@Schema(description = "成交金额")
	private BigDecimal dealAmount;

	/**
	 * 运费
	 */
	@Schema(description = "运费")
	private BigDecimal freightPrice;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;

	/**
	 * 商品数量
	 */
	@Schema(description = "商品数量")
	private Integer quantity;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 订单id
	 */
	@Schema(description = "订单id")
	private Long orderId;

}
