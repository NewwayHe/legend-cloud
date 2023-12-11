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

/**
 * 移动端基础设置
 *
 * @author legendshop
 */
@Schema(description = "移动端基础设置DTO")
@Data
public class AppSettingDTO {

	/**
	 * 风格主题名称
	 */
	@Schema(description = "风格主题名称")
	@NotBlank(message = "请选择主题风格")
	private String theme;

	/**
	 * 主题颜色
	 */
	@Schema(description = "主题颜色")
	@NotBlank(message = "请选择主题风格")
	private String color;

	/**
	 * 分类设置
	 */
	@Schema(description = "分类设置")
	private String categorySetting;
}
