/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.DoubleSerialize;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 店铺收藏表(FavoriteShop)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "店铺收藏")
public class FavoriteShopDTO implements Serializable {


	private static final long serialVersionUID = -5369576395279350962L;
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
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	@NotNull(message = "shopId不能为空")
	private Long shopId;


	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 店铺图片
	 */
	@Schema(description = "店铺图片")
	private String shopPic;

	/**
	 * 状态 {@link ShopDetailStatusEnum}
	 */
	@Schema(description = "-1：关闭店铺 0：店铺下线 1：上线正常营业")
	private String status;


	/**
	 * 收藏数量
	 */
	@Schema(description = "收藏数量")
	private Long favoriteCount;


	/**
	 * 收藏时间
	 */
	@Schema(description = "收藏时间")
	private Date recDate;


	/**
	 * 是否取消收藏 false:取消  true：新增
	 */
	@Schema(description = "是否取消收藏 false:取消  true：新增")
	private Boolean collectionFlag;


	/**
	 * 热销商品
	 */
	@Schema(description = "热销商品")
	private List<HotProductDTO> items;


	/**
	 * 收藏id
	 */
	@Schema(description = "收藏ids")
	private Long[] selectedFavs;

	/**
	 * 物流评分
	 */
	@Schema(description = "物流评分")
	@JsonSerialize(using = DoubleSerialize.class)
	private Double dvyTypeAvg;

	/**
	 * 服务评分
	 */
	@Schema(description = "服务评分")
	@JsonSerialize(using = DoubleSerialize.class)
	private Double shopCommAvg;

	/**
	 * 描述评分
	 */
	@Schema(description = "描述评分")
	@JsonSerialize(using = DoubleSerialize.class)
	private Double productCommentAvg;

	/**
	 * 综合评分
	 */
	@Schema(description = "综合评分")
	@JsonSerialize(using = DoubleSerialize.class)
	private Double score;
}
