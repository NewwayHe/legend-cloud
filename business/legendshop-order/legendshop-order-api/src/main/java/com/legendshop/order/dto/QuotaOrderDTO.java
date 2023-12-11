/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "限购订单")
public class QuotaOrderDTO {
	@Schema(description = "是否限购(null:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)")
	private String quotaType;

	/**
	 * 限购数量
	 */
	@Schema(description = "限购数量")
	private Integer quotaCount;

	/**
	 * 限购时间
	 */
	@Schema(description = "限购时间")
	private Date quotaTime;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 商品id
	 */
	@Schema(description = "productId")
	private Long productId;
}
