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
 * 装修点击动作
 *
 * @author legendshop
 */
@Schema(description = "装修点击动作")
@Data
public class AppDecorateActionDTO {

	/**
	 * 动作类型
	 */
	@Schema(description = "动作类型")
	private String type;

	/**
	 * 子类型
	 */
	@Schema(description = "子类型")
	private String subType;

	/**
	 * 动作目标
	 */
	@Schema(description = "动作目标")
	private String target;

}
