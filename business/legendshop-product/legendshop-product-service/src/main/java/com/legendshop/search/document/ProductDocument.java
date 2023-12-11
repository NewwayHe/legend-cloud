/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ActivitySkuDTO;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.search.dto.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索的索引实体
 *
 * @author legendshop
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "#{@productIndexName}")
public class ProductDocument implements Serializable {

	private static final long serialVersionUID = -3692028501531645822L;

	@Id
	@Field(type = FieldType.Long)
	private Long productId;

	@Field(type = FieldType.Long)
	private Long shopId;

//	@Field(type = FieldType.Nested)
//	private List<Long> activityIds;

	/**
	 * 所有需要被搜索的信息，包含标题，分类，甚至品牌
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String keyword;

	/**
	 * 商品名称
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String productName;

	/**
	 * 店铺名称
	 */
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String shopName;

	/**
	 * 商品主图
	 */
	@Field(type = FieldType.Text)
	private String productPic;

	/**
	 * 图片路径
	 */
	@Field(type = FieldType.Text)
	private String imgPath;

	/**
	 * 主规格id
	 */
	@Field(type = FieldType.Long)
	private Long mainSpecificationId;

	/**
	 * 规格样式 {@link com.legendshop.product.enums.ProductSpecificationStyle}
	 */
	@Field(type = FieldType.Text)
	private String specificationStyle;

	/**
	 * 商品规格
	 */
	@Field(type = FieldType.Text)
	private String specification;

	/**
	 * 参数组
	 */
	@Field(type = FieldType.Text)
	private String paramGroup;

	/**
	 * 用户自定义参数组
	 */
	@Field(type = FieldType.Text)
	private String userParamGroup;


	/**
	 * 商品动态参数
	 */
	@Field(type = FieldType.Text)
	private String parameter;

	/**
	 * 用户商品动态参数
	 */
	@Field(type = FieldType.Text)
	private String userParameter;

	/**
	 * 商品名称建议词搜索
	 */
	private String productNameSuggest;

	/**
	 * 评论数
	 */
	@Field(type = FieldType.Long)
	private Long comments;

	/**
	 * (全局商城)一级分类
	 */
	@Field(type = FieldType.Long)
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	@Field(type = FieldType.Long)
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	@Field(type = FieldType.Long)
	private Long globalThirdCatId;

	/**
	 * (商家小分类)一级分类
	 */
	@Field(type = FieldType.Long)
	private Long shopFirstCatId;

	/**
	 * (商家小分类)二级分类
	 */
	@Field(type = FieldType.Long)
	private Long shopSecondCatId;

	/**
	 * (商家小分类)三级分类
	 */
	@Field(type = FieldType.Long)
	private Long shopThirdCatId;

	/**
	 * 卖点 简介
	 */
	@Field(type = FieldType.Text)
	private String brief;

	/**
	 * 商品视频
	 **/
	@Field(type = FieldType.Text)
	private String video;

	/**
	 * 品牌id
	 **/
	@Field(type = FieldType.Long)
	private Long brandId;

	/**
	 * 品牌名称
	 **/
	@Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
	private String brandName;


	/**
	 * 品牌logo
	 **/
	@Field(type = FieldType.Text)
	private String brandPic;

	/**
	 * 商品价格
	 */
	@Field(type = FieldType.Double)
	private BigDecimal price;


	/**
	 * 商品限时折扣价格
	 */
	@Field(type = FieldType.Double)
	private BigDecimal discountPrice;

	/**
	 * 原价
	 */
	@Field(type = FieldType.Double)
	private BigDecimal originalPrice;

	/**
	 * 可搜索的参数，key是参数名，值是参数值{内存:[32G,64G]}
	 */
	private Map<String, List<Long>> ev;

	/**
	 * 浏览数
	 */
	@Field(type = FieldType.Integer)
	private Integer views;

	/**
	 * 购买数
	 */
	@Field(type = FieldType.Integer)
	private Integer buys;


	/**
	 * 综合权重
	 */
	@Field(type = FieldType.Double)
	private Long multiple;

	//	@Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
	/**
	 * 创建时间 yyyyMMdd'T'HHmmss
	 */
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
	@JsonFormat(pattern = "yyyyMMdd'T'HHmmss")
	private Date createTime;

	/**
	 * 分销类型 0、非分销商品  1、分销商品
	 */
	@Field(type = FieldType.Integer)
	private Integer distType;

	/**
	 * 商品介绍
	 */
	@Field(type = FieldType.Text)
	private String content;

	/**
	 * 预售标识
	 */
	@Field(type = FieldType.Boolean)
	private Boolean preSellFlag;

	/**
	 * 预售信息
	 */
	@Field(type = FieldType.Object)
	private PreSellProductDTO preSellProductMessage;

	/**
	 * 商品图片集合
	 **/
	@Field(type = FieldType.Text)
	private List<String> productPics;

//	/**
//	 * 商品规格集合
//	 **/
//	@Field(type = FieldType.Nested)
//	private List<SearchProductParamDTO> specificationGroup;
//
//	/**
//	 * 商品参数组集合
//	 **/
//	@Field(type = FieldType.Nested)
//	private List<SearchProductParamDTO> paramGroup;

	/**
	 * 商品SKU集合
	 **/
	@Field(type = FieldType.Nested)
	private List<SkuBO> skuList;

	/**
	 * 标签集合
	 */
	@Field(type = FieldType.Nested)
	private List<Tag> tagList;

	@Field(type = FieldType.Nested)
	private List<ActivitySkuDTO> activitySkuList;

	/**
	 * 分销比列
	 */
	private BigDecimal ratio;

	/**
	 * 商品状态: -2：永久删除；-1：删除；0：下线；1：上线；2：违规；3：全部
	 * {@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	private Integer status;

	/**
	 * 审核状态 -1：拒绝；0：待审核 ；1：通过
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	private Integer opStatus;

	/**
	 * 删除操作状态 -2：永久删除；-1：删除；1：正常；
	 * {@link ProductDelStatusEnum}
	 */
	private Integer delStatus;

	/**
	 * 店铺状态
	 * ShopDetailStatusEnum
	 */
	private Integer shopStatus;

	private Long skuId;

	private List<Long> skuIdList;

	/**
	 * 运费模板ID
	 */
	private Long transId;
	/**
	 * 配送方式 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	private Integer deliveryType;
}




