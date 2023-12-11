/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.product.enums.ProductStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品收藏表(Favorite)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品收藏")
public class FavoriteProductDTO implements Serializable {

	private static final long serialVersionUID = -16354091432798017L;

	/**
	 * 主键
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	@NotNull(message = "商品id不能为空")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品价格
	 */
	@Schema(description = "商品价格")
	private String price;


	/**
	 * 图片
	 */
	@Schema(description = "图片")
	private String pic;


	/**
	 * 收藏时间
	 */
	@Schema(description = "收藏时间")
	private Date addtime;

	/**
	 * 是否取消收藏 false:取消  true：新增
	 */
	@Schema(description = "是否取消收藏 false:取消  true：新增")
	@NotNull(message = "是否取消收藏不能为空")
	private Boolean collectionFlag;


	/**
	 * 收藏数量
	 */
	@Schema(description = "收藏数量")
	private Long favoriteCount;

	/**
	 * 状态 {@link ProductStatusEnum}
	 */
	@Schema(description = "[状态 -2：商家永久删除状态 -1： 商品删除状态 (放商品回收站) 0：仓库中的商品，用户点击下线 1：上线的商品，正常销售的商品 2：商品违规下线 3：商品全部状态]")
	private String status;


	/**
	 * 收藏id
	 */
	@Schema(description = "收藏ids")
	private Long[] selectedFavs;

	@Schema(description = "最低价")
	private BigDecimal minPrice;

	@Schema(description = "最高价")
	private BigDecimal maxPrice;

}
