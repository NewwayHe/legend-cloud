/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivitySkuDTO;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索的文档DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品搜索的文档DTO")
public class ProductDocumentDTO implements Serializable {

	private static final long serialVersionUID = -1744128624536775463L;

	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "店铺ID")
	private Long shopId;

	@Schema(description = "运费模板ID")
	private Long transId;

	@Schema(description = "营销活动Ids")
	private List<Long> activityIds;

	@Schema(description = "所有需要被搜索的信息，包含标题，分类，甚至品牌")
	private String keyword;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商家名称")
	private String shopName;

	@Schema(description = "商品主图")
	private String productPic;

	@Schema(description = "商品名称")
	private String productName_suggest;

	/**
	 * 商品规格
	 */
	@Schema(description = "商品规格")
	private String specification;

	/**
	 * 主规格id
	 */
	@Schema(description = "主规格id")
	private Long mainSpecificationId;

	/**
	 * 规格样式 {@link com.legendshop.product.enums.ProductSpecificationStyle}
	 */
	@Schema(description = "规格样式 PIC：图片样式；TXT：文本样式")
	private String specificationStyle;


	/**
	 * 参数组
	 */
	@Schema(description = "参数组")
	private String paramGroup;

	/**
	 * 用户自定义参数组
	 */
	@Schema(description = "用户自定义参数组")
	private String userParamGroup;

	/**
	 * 商品动态参数
	 */
	@Schema(description = "商品动态参数")
	private String parameter;

	/**
	 * 用户自定义商品动态参数
	 */
	@Schema(description = "用户自定义商品动态参数")
	private String userParameter;

	/**
	 * (全局商城)一级分类
	 */
	@Schema(description = "(全局商城)一级分类")
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	@Schema(description = "(全局商城)二级分类")
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	@Schema(description = "(全局商城)三级分类")
	private Long globalThirdCatId;

	/**
	 * (商家小分类)一级分类
	 */
	@Schema(description = "(商家小分类)一级分类")
	private Long shopFirstCatId;

	/**
	 * (商家小分类)二级分类
	 */
	@Schema(description = "(商家小分类)二级分类")
	private Long shopSecondCatId;

	/**
	 * (商家小分类)三级分类
	 */
	@Schema(description = "(商家小分类)三级分类")
	private Long shopThirdCatId;

	@Schema(description = "卖点 简介")
	private String brief;

	@Schema(description = "品牌id")
	private Long brandId;

	/**
	 * 品牌名称
	 **/
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 品牌logo
	 **/
	@Schema(description = "品牌logo")
	private String brandPic;

	/**
	 * 商品视频
	 **/
	@Schema(description = "商品视频")
	private String video;


	/**
	 * 价格
	 */
	@Schema(description = "价格")
	private BigDecimal price;

	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private BigDecimal originalPrice;

	/**
	 * 活动价格
	 */
	@Schema(description = "限时折扣价格")
	private BigDecimal discountPrice;

//	@Schema(description = "转换后的obj对象")
//	private List<SearchSkuDTO> skus = EMPTY_LIST;

	@Schema(description = "可搜索的规格参数，key是参数名，值是参数值{内存:[32G,64G]}")
	private Map<String, List<Long>> ev;

	@Schema(description = "浏览数")
	private Long views;

	@Schema(description = "购买数")
	private Long buys;

	@Schema(description = "评论数")
	private Long comments;

	@Schema(description = "综合权重")
	private Long multiple;


	@Schema(description = "商品发布时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 分销类型 0、非分销商品  1、分销商品
	 */
	@Schema(description = "分销类型 0、非分销商品  1、分销商品")
	private Integer distType;

	/**
	 * 商品介绍
	 */
	private String content;

	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态: -2：永久删除；-1：删除；0：下线；1：上线；2：违规；3：全部")
	private Integer status;

	/**
	 * 审核操作状态 {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;

	/**
	 * 店铺状态
	 * {@link ShopDetailStatusEnum}
	 */
	@Schema(description = "店铺状态")
	private Integer shopStatus;

	/**
	 * 图片路径
	 */
	@Schema(description = "图片路径")
	private String imgPath;

	/**
	 * 商品图片集合
	 **/
	@Schema(description = "商品图片集合")
	private List<String> productPics;

	/**
	 * 预售标识
	 */
	@Schema(description = "预售标识")
	private Boolean preSellFlag;

	/**
	 * 预售信息
	 */
	@Schema(description = "预售信息")
	private PreSellProductDTO preSellProductMessage;

//	/**
//	 * 商品规格集合
//	 **/
//	@Schema(description = "商品规格集合")
//	private List<ProductPropertyBO> specificationGroup;
//
//
//	/**
//	 * 商品参数组集合
//	 **/
//	@Schema(description = "商品参数组集合")
//	private List<ProductPropertyBO> paramGroup;

	/**
	 * 商品SKU集合
	 **/
	@Schema(description = "SKU集合")
	private List<SkuBO> skuList;


	/**
	 * 商品SKU集合
	 **/
	@Schema(description = "标签集合")
	private List<Tag> tagList;

	/**
	 * 商品SKU 营销信息
	 **/
	@Schema(description = "商品SKU 营销信息")
	private List<ActivitySkuDTO> activitySkuList;

	/**
	 * 分销比列
	 */
	@Schema(description = "分销比列")
	private BigDecimal ratio;

	@Schema(description = "是否收藏")
	private Boolean favourite = false;

	/**
	 * 配送方式 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送 10:到店自提  20:商家配送和及到店自提")
	private Integer deliveryType;
}
