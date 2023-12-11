/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SPU草稿表(DraftProduct)实体类
 *
 * @author legendshop
 * @since 2022-05-08 10:11:55
 */
@Data
@Entity
@Table(name = "ls_draft_product")
public class DraftProduct extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 185020735862064747L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "draftProduct_SEQ")
	private Long id;

	/**
	 * 商家ID
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 店铺用户id
	 */
	@Column(name = "shop_user_id")
	private Long shopUserId;

	/**
	 * 品牌
	 */
	@Column(name = "brand_id")
	private Long brandId;

	/**
	 * (全局商城)一级分类
	 */
	@Column(name = "global_first_cat_id")
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	@Column(name = "global_second_cat_id")
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	@Column(name = "global_third_cat_id")
	private Long globalThirdCatId;

	/**
	 * (商家小分类)一级分类
	 */
	@Column(name = "shop_first_cat_id")
	private Long shopFirstCatId;

	/**
	 * (商家小分类)二级分类
	 */
	@Column(name = "shop_second_cat_id")
	private Long shopSecondCatId;

	/**
	 * (商家小分类)三级分类
	 */
	@Column(name = "shop_third_cat_id")
	private Long shopThirdCatId;

	/**
	 * 是否预售商品
	 */
	@Column(name = "pre_sell_flag")
	private Boolean preSellFlag;

	/**
	 * 商品上架方式：商品上架方式 -1：不上架，0：预约上架，1：审核通过后马上上架
	 */
	@Column(name = "on_sale_way")
	private Integer onSaleWay;

	/**
	 * 预约上架
	 */
	@Column(name = "appoint")
	private String appoint;

	/**
	 * 商品名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 规格样式【TXT：文本样式 PIC：图片样式 GRAPHIC：图文样式】
	 */
	@Column(name = "specification_style")
	private String specificationStyle;

	/**
	 * 销售价范围
	 */
	@Column(name = "price")
	private String price;

	/**
	 * 最低价格
	 */
	@Column(name = "min_price")
	private BigDecimal minPrice;

	/**
	 * 最高价格
	 */
	@Column(name = "max_price")
	private BigDecimal maxPrice;

	/**
	 * 商家编码
	 */
	@Column(name = "party_code")
	private String partyCode;

	/**
	 * 草稿状态【-10: 商品未发布、-10: 拒绝、0: 待审核、1: 通过 】
	 * DraftProductStatusEnum
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 简要描述,卖点等
	 */
	@Column(name = "brief")
	private String brief;

	/**
	 * 详细描述(手机端)
	 */
	@Column(name = "content_m")
	private String contentM;

	/**
	 * 详细描述
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 库存警告
	 */
	@Column(name = "stocks_arm")
	private Integer stocksArm;

	/**
	 * 商品类型，E.普通商品，V:虚拟商品
	 */
	@Column(name = "prod_type")
	private String prodType;

	/**
	 * 关键字
	 */
	@Column(name = "key_word")
	private String keyWord;

	/**
	 * 参数属性
	 */
	@Column(name = "parameter")
	private String parameter;

	/**
	 * 用户自定义参数属性
	 */
	@Column(name = "user_parameter")
	private String userParameter;

	/**
	 * 参数组
	 */
	@Column(name = "param_group")
	private String paramGroup;

	/**
	 * 用户自定义参数组
	 */
	@Column(name = "user_param_group")
	private String userParamGroup;

	/**
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 */
	@Column(name = "stock_counting")
	private Boolean stockCounting;

	/**
	 * 商品搜索主图片
	 */
	@Column(name = "pic")
	private String pic;

	/**
	 * 商品图片集合
	 */
	@Column(name = "img_path")
	private String imgPath;

	/**
	 * 视频
	 */
	@Column(name = "video")
	private String video;

	/**
	 * 运费模板id
	 */
	@Column(name = "trans_id")
	private Long transId;

	/**
	 * 主规格id
	 */
	@Column(name = "main_specification_id")
	private Long mainSpecificationId;

	/**
	 * 规格属性
	 */
	@Column(name = "specification")
	private String specification;

	/**
	 * 版本号
	 */
	@Column(name = "version")
	private Integer version;

	/**
	 * 是否多规格(0单规格，1多规格)
	 */
	@Column(name = "multiple_specification_flag")
	private Boolean multipleSpecificationFlag;

	/**
	 * 是否限购(null:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)
	 */
	@Column(name = "quota_type")
	private String quotaType;

	/**
	 * 限购数量
	 */
	@Column(name = "quota_count")
	private Integer quotaCount;

	/**
	 * 限购时间
	 */
	@Column(name = "quota_time")
	private Date quotaTime;

	/**
	 * 规格图片
	 */
	@Column(name = "property_image")
	private String propertyImage;

	/**
	 * 预售商品信息
	 */
	@Column(name = "pre_sell_product")
	private String preSellProduct;

	/**
	 * 用户自定义参数
	 */
	@Column(name = "custom_property_value")
	private String customPropertyValue;
}
