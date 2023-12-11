/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类设置DTO
 *
 * @author legendshop
 */
@Data
public class CategorySettingDTO implements Serializable {

	private static final long serialVersionUID = 5135124015839994969L;

	/**
	 * {@link com.legendshop.shop.enums.DecorateSettingEnum}
	 */
	@Schema(description = "分类（1:一级、2:二级、3:三级")
	@NotNull(message = "展示类目不能为空")
	private Integer category;

	/**
	 * {@link com.legendshop.shop.enums.DecorateSettingEnum}
	 */
	@Schema(description = "规则（1:有图 0:没图）")
	@NotNull(message = "展示类目不能为空")
	private Integer schema;

	/**
	 * {@link com.legendshop.shop.enums.DecorateSettingEnum}
	 */
	@Schema(description = "是否展示分类广告")
	@NotNull(message = "展示类目不能为空")
	private Integer showCatAdvert;

	/**
	 * {@link com.legendshop.shop.enums.DecorateSettingEnum}
	 */
	@Schema(description = "规则（1:有商品列表 0:无商品列表）")
	@NotNull(message = "是否展示商品列表")
	private Integer goods;


}
