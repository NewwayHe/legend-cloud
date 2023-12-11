/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品搜索的文档DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "店铺搜索的文档DTO")
public class ShopDocumentDTO implements Serializable {


	private static final long serialVersionUID = 3693327976770931829L;

	@Schema(description = "店铺ID")
	private Long shopId;

	@Schema(description = "店铺名字")
	private String shopName;

	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Schema(description = "店铺类型0.专营店1.旗舰店2.自营店")
	private Integer shopType;

	/**
	 * 店铺入驻类型  2：供应商  3:分销商   {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "店铺入驻类型  2：供应商  3:分销商")
	private Integer applyForType;


	@Schema(description = "店铺图片")
	private String pic;

	/**
	 * 物流评分
	 */
	@Schema(description = "物流评分")
	private BigDecimal dvyTypeAvg;

	/**
	 * 服务评分
	 */
	@Schema(description = "服务评分")
	private BigDecimal shopCommAvg;

	/**
	 * 描述评分
	 */
	@Schema(description = "描述评分")
	private BigDecimal productCommentAvg;

	/**
	 * 综合评分
	 */
	@Schema(description = "综合评分")
	private BigDecimal score;
}
