/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "店铺信息VO")
public class ShopInfoVO implements Serializable {


	@Schema(description = "店铺id")
	private Long id;

	/**
	 * 商城名称
	 */
	@Schema(description = "商城名称")
	private String shopName;

	/**
	 * 店铺Logo
	 */
	@Schema(description = "店铺Logo")
	private String shopAvatar;

	/**
	 * 行业名称
	 */
	@Schema(description = "行业名称")
	private String industryDirectoryName;


	@Schema(description = "新手引导状态 0:新手,1:非新手")
	private Integer shopNewBieStatus;
}
