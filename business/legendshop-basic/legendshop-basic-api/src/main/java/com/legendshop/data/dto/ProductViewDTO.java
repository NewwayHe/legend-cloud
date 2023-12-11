/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品访问记录(ProductView)DTO
 *
 * @author legendshop
 * @since 2021-03-24 17:21:56
 */
@Data
@Schema(description = "商品访问记录DTO")
@Accessors(chain = true)
public class ProductViewDTO implements Serializable {

	private static final long serialVersionUID = 889993886453677418L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;

	/**
	 * 商家id
	 */
	@Schema(description = "商家id")
	private Long shopId;

	/**
	 * 商家名称
	 */
	@Schema(description = "商家名称")
	private String shopName;

	/**
	 * 产品id
	 */
	@Schema(description = "产品id")
	private Long productId;

	/**
	 * 产品名称
	 */
	@Schema(description = "产品名称")
	private String productName;

	/**
	 * 访问总人数
	 */
	@Schema(description = "访问总人数")
	private Integer viewPeople;

	/**
	 * 访问总次数
	 */
	@Schema(description = "访问总次数")
	private Integer viewFrequency;

	/**
	 * 加入购物车数量
	 */
	@Schema(description = "加入购物车数量")
	private Integer cartNum;

	/**
	 * 商品收藏数
	 */
	@Schema(description = "商品收藏数")
	private Integer favoriteNum;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;


}
