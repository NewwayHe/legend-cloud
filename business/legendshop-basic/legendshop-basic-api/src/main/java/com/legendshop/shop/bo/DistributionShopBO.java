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
import java.util.Date;

/**
 * 分销商店信息(DistributionShop)BO
 *
 * @author legendshop
 * @since 2021-03-11 17:07:15
 */
@Data
public class DistributionShopBO implements Serializable {

	private static final long serialVersionUID = -19010047397847693L;

	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户联系电话
	 */
	@Schema(description = "用户联系电话")
	private String userMobile;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 商家用户ID
	 */
	@Schema(description = "商家用户ID")
	private Long shopUserId;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

}
