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
import com.legendshop.product.enums.ProductDelStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品(Prod)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product")
public class Product extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -8167360815412017324L;

	/**
	 * 产品ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_SEQ")
	private Long id;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 商家用户ID
	 */
	@Column(name = "shop_user_Id")
	private Long shopUserId;

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
	 * 商品上架方式
	 * {@link com.legendshop.product.enums.ProductOnSaleWayEnum}
	 */
	@Column(name = "on_sale_way")
	private Integer onSaleWay;

	/**
	 * 预约上架id
	 */
	@Column(name = "appoint_id")
	private Long appointId;

	/**
	 * 规格样式 {@link com.legendshop.product.enums.ProductSpecificationStyle}
	 */
	@Column(name = "specification_style")
	private String specificationStyle;

	/**
	 * 商品视频
	 */
	@Column(name = "video")
	private String video;


	/**
	 * 商家编码
	 */
	@Column(name = "party_code")
	private String partyCode;


	/**
	 * 商品名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 销售价范围
	 */
	@Column(name = "price")
	private String price;

	/**
	 * 销售起步价
	 */
	@Column(name = "min_price")
	private BigDecimal minPrice;

	/**
	 * 销售最高价
	 */
	@Column(name = "max_price")
	private BigDecimal maxPrice;


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
	 * 观看人数，同一个IP半个小时之类只算一次
	 */
	@Column(name = "views")
	private Integer views;


	/**
	 * 已经销售数量
	 */
	@Column(name = "buys")
	private Integer buys;


	/**
	 * 评论数
	 */
	@Column(name = "comments")
	private Integer comments;

	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Column(name = "op_status")
	private Integer opStatus;


	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Column(name = "del_status")
	private Integer delStatus;

	/**
	 * 库存量
	 */
	@Column(name = "stocks")
	private Integer stocks;


	/**
	 * 库存警告
	 */
	@Column(name = "stocks_arm")
	private Integer stocksArm;


	/**
	 * 商品类型，P.普通商品，G:团购商品，S:二手商品，D:打折商品
	 */
	@Column(name = "prod_type")
	private String prodType;

	/**
	 * 是否多规格(0单规格，1多规格)
	 */
	@Column(name = "multiple_specification_flag")
	private Boolean multipleSpecificationFlag;

	/**
	 * 关键字
	 */
	@Column(name = "key_word")
	private String keyWord;


	/**
	 * 商品动态参数
	 */
	@Column(name = "parameter")
	private String parameter;


	/**
	 * 用户自定义参数
	 */
	@Column(name = "user_parameter")
	private String userParameter;

	/**
	 * 商品规格
	 */
	@Column(name = "specification")
	private String specification;

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
	 * 品牌
	 */
	@Column(name = "brand_id")
	private Long brandId;


	/**
	 * 实际库存
	 */
	@Column(name = "actual_stocks")
	private Integer actualStocks;


	/**
	 * 评论得分
	 */
	@Column(name = "review_scores")
	private Integer reviewScores;

	/**
	 * 运费模板ID
	 */
	@Column(name = "trans_id")
	private Long transId;

	/**
	 * 库存计数方式，false：拍下减库存，true：付款减库存
	 */
	@Column(name = "stock_counting")
	private Boolean stockCounting;

	/**
	 * 微信小程序码
	 */
	@Column(name = "wx_a_code")
	private String wxACode;


	/**
	 * 商家端微信小程序码
	 */
	@Column(name = "shop_wx_code")
	private String shopWxCode;

	/**
	 * 图片路径
	 */
	@Column(name = "img_path")
	private String imgPath;

	/**
	 * 小图片
	 */
	@Column(name = "pic")
	private String pic;

	/**
	 * 主规格id
	 */
	@Column(name = "main_specification_id")
	private Long mainSpecificationId;


	/**
	 * 版本号
	 */
	@Column(name = "version")
	private Integer version;

	/**
	 * 是否限购(N:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)
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

	@Column(name = "audit_opinion")
	private String auditOpinion;

	/**
	 * 是否参与分销
	 */
	@Column(name = "distribution_flag")
	private Integer distributionFlag;

	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Column(name = "delivery_type")
	private Integer deliveryType;
}
