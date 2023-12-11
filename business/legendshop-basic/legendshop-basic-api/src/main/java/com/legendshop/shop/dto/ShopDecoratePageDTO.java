/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺主页装修DTO
 *
 * @author legendshop
 */
@Data
public class ShopDecoratePageDTO implements Serializable {


	private static final long serialVersionUID = -8892008596533688439L;

	/**
	 * 主键ID
	 */
	private Long id;


	/**
	 * 所属商家ID
	 */
	private Long shopId;


	/**
	 * 页面名称
	 */
	@NotBlank(message = "页面名不能为空")
	private String name;


	/**
	 * 页面类型 [INDEX：首页 POSTER：海报页] 参考枚举DecoratePageCategoryEnum
	 */
	@NotBlank(message = "页面类型不能为空")
	private String category;

	/**
	 * 可编辑的装修数据
	 */
	@NotBlank(message = "装修数据不能为空")
	private String data;


	/**
	 * 来源[pc：pc端 mobile：移动端]参考枚举DecoratePageSourceEnum
	 */
	@NotBlank(message = "装修来源不能为空")
	private String source;

	/**
	 * 封面图
	 */
	private String coverPicture;

	/**
	 * 类型   1 官方  2 原创
	 */
	private Integer type;
}
