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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量收藏商品
 *
 * @author legendshop
 * @create: 2021-02-20 10:40
 */
@Data
public class BatchFavoriteProductDTO implements Serializable {

	private static final long serialVersionUID = 4399019591998458121L;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	@NotNull(message = "商品ID不能为空")
	private List<Long> productIds;

	/**
	 * 是否取消收藏 false:取消  true：新增
	 */
	@Schema(description = "是否取消收藏 false:取消  true：新增")
	@NotNull(message = "是否取消收藏不能为空")
	private Boolean collectionFlag;

	@Schema(description = "来源")
	private String source;
}
