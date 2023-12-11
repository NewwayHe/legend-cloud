/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.shop.bo.ShopDetailBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品快照(ProdSnapshot)实体类
 *
 * @author legendshop
 */
@Schema(description = "商品快照DTO")
@Data
public class ProductSnapshotDTO implements Serializable {


	private static final long serialVersionUID = 1394069474111212447L;
	/**
	 * 快照ID
	 */
	@Schema(description = "快照ID")
	private Long id;


	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long productId;


	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;


	/**
	 * 版本号
	 */
	@Schema(description = "版本号")
	private Integer version;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String name;

	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private BigDecimal originalPrice;

	/**
	 * 现价
	 */
	@Schema(description = "现价")
	private BigDecimal price;


	/**
	 * 简要描述,卖点等
	 */
	@Schema(description = "简要描述,卖点等")
	private String brief;


	/**
	 * 详细描述
	 */
	@Schema(description = "详细描述")
	private String content;


	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;


	/**
	 * 属性json
	 */
	@Schema(description = "属性json")
	private String attribute;


	/**
	 * 参数json
	 */
	@Schema(description = "参数json")
	private String parameter;

	/**
	 * 参数组json
	 */
	@Schema(description = "参数组json")
	private String paramGroup;


	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	private String brandName;


	/**
	 * 商品类型，E.普通商品，V:虚拟商品
	 */
	@Schema(description = "商品类型，E.普通商品，V:虚拟商品")
	private String productType;


	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date createTime;


	/**
	 * 购买数量
	 */
	@Schema(description = "购买数量")
	private Integer productCount;

	@Schema(description = "商品参数组集合")
	private List<ProductPropertyBO> paramGroupBOList;

	/**
	 * 店铺信息的BO对象
	 */
	@Schema(description = "店铺信息的BO对象")
	private ShopDetailBO shopDetailBO;

}
