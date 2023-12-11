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
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺搜索DTO
 *
 * @author legendshop
 */
@Data
public class SearchShopDTO implements Serializable {

	private static final long serialVersionUID = 2085604462918049821L;
	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 店铺类型0.专营店1.旗舰店2.自营店
	 */
	@Schema(description = "店铺类型 0.专营店1.旗舰店2.自营店")
	private Integer shopType;

	/**
	 * 主体申请类型，0，个人。  1，商家
	 * {@link com.legendshop.shop.enums.ApplyForTypeEnum}
	 */
	@Schema(description = "主体申请类型.1:个人、2:商家、3:分销员")
	private Integer applyForType;

	/**
	 * 店铺头像
	 */
	@Schema(description = "店铺头像")
	private String shopAvatar;

	/**
	 * 销售数
	 */
	@Schema(description = "销售数")
	private Integer buys;

	/**
	 * 店铺评分
	 */
	@Schema(description = "店铺评分")
	private BigDecimal credit;

	/**
	 * 行业名
	 */
	@Schema(description = "行业名")
	private String industryDirectoryName;

	@Schema(description = "店铺完整地址")
	private String shopCompleteAddress;

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
	 * 商品信息
	 */
	@Schema(description = "商品信息")
	private List<SearchShopProductDTO> product;

	/**
	 * 是否收藏
	 */
	@Schema(description = "是否收藏 true 收藏 false 不收藏")
	private Boolean collectionFlag;


	/**
	 * 好评率
	 */
	@Schema(description = "好评率")
	private BigDecimal favorableRate;
}
