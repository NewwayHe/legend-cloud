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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * 装修页面DTO
 *
 * @author legendshop
 */
@Schema(description = "装修页面DTO")
@Data
public class DecoratePageDTO implements Serializable {


	private static final long serialVersionUID = 7674443652537395206L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 页面名称
	 */
	@Schema(description = "页面名称")
	@NotBlank(message = "页面名不能为空")
	private String name;


	/**
	 * 页面类型 [INDEX：首页 POSTER：海报页] 参考枚举DecoratePageCategoryEnum
	 */
	@Schema(description = "页面类型 [INDEX：首页 POSTER：海报页 PERSONAL_CENTER：个人中心]")
	@NotBlank(message = "页面类型不能为空")
	private String category;

	/**
	 * 可编辑的装修数据
	 */
	@Schema(description = "可编辑的装修数据")
	@NotBlank(message = "装修数据不能为空")
	private String data;


	/**
	 * 来源[pc：pc端 mobile：移动端]参考枚举DecoratePageSourceEnum
	 */
	@Schema(description = "来源[pc：pc端 mobile：移动端]")
	@NotBlank(message = "装修来源不能为空")
	private String source;

	/**
	 * 封面图
	 */
	@Schema(description = "封面图")
	private String coverPicture;

	@Schema(description = "类型 1 官方  2 原创")
	private Integer type;
}
