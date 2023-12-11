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
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌推荐.
 *
 * @author legendshop
 */
@Data
@Schema(description = "品牌推荐")
public class RecommBrandDTO implements Serializable {


	/**
	 * 品牌ID
	 */
	@Schema(description = "品牌ID")
	private Long brandId;

	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 品牌图片
	 */
	@Schema(description = "品牌图片")
	private String brandPic;

}
