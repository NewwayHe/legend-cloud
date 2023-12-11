/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "店铺列表信息DTO")
public class ShopListDTO {

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long id;

	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String shopName;


	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;

	/**
	 * 店铺简述
	 */
	@Schema(description = "店铺简述")
	private String briefDesc;

	/**
	 * 用户昵称
	 */
	@Schema(description = "昵称")
	private String nickName;
}
