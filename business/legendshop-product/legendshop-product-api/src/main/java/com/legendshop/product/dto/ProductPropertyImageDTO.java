/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 属性图片
 *
 * @author legendshop
 */
@Data
@Schema(description = "属性图片")
public class ProductPropertyImageDTO implements Serializable {


	private static final long serialVersionUID = -8564281837743445244L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 属性ID
	 */
	@Schema(description = "属性ID")
	@NotNull(message = "属性ID不能为空")
	private Long propId;

	/**
	 * 属性值ID
	 */
	@Schema(description = "属性值ID")
	@NotNull(message = "属性值ID不能为空")
	private Long valueId;

	/**
	 * 属性值名称
	 */
	@Schema(description = "属性值名称")
	@NotBlank(message = "属性值名称不能为空")
	private String valueName;

	/**
	 * 图片Url
	 */
	@Schema(description = "图片Url")
	private String url;

	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	@NotNull(message = "顺序不能为空")
	private Long seq;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createDate;

	/**
	 * 图片路径 集合
	 */
	@Schema(description = "图片路径 集合")
	private List<String> imgList;

} 
