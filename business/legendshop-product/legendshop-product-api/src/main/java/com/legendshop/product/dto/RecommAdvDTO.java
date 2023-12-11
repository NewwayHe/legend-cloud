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
 * 广告位DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "广告位DTO")
public class RecommAdvDTO implements Serializable {

	/**
	 * 链接
	 */
	@Schema(description = "链接")
	private String link;

	/**
	 * 图片
	 */
	@Schema(description = "图片")
	private String advPic;

}
