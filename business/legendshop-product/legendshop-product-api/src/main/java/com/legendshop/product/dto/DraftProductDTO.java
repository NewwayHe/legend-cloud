/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品SPU草稿表(DraftProduct)DTO
 *
 * @author legendshop
 * @since 2022-05-08 10:13:19
 */
@Data
@Schema(description = "商品SPU草稿表DTO")
public class DraftProductDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -26397369802560420L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long productId;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 店铺用户id
	 */
	@Schema(description = "店铺用户id")
	private Long shopUserId;

	/**
	 * 品牌
	 */
	@Schema(description = "品牌")
	private Long brandId;

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

	/**
	 * 是否预售商品
	 */
	@Schema(description = "是否预售商品")
	private Boolean preSellFlag;

	/**
	 * 商品上架方式：商品上架方式 -1：不上架，0：预约上架，1：审核通过后马上上架
	 */
	@Schema(description = "商品上架方式：商品上架方式 -1：不上架，0：预约上架，1：审核通过后马上上架")
	private Integer onSaleWay;

	/**
	 * 预约上架
	 */
	@Schema(description = "预约上架")
	private String appoint;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String name;

	/**
	 * 规格样式【TXT：文本样式 PIC：图片样式 GRAPHIC：图文样式】
	 */
	@Schema(description = "规格样式【TXT：文本样式 PIC：图片样式 GRAPHIC：图文样式】")
	private String specificationStyle;

	/**
	 * 销售价范围
	 */
	@Schema(description = "销售价范围")
	private String price;

	/**
	 * 最低价格
	 */
	@Schema(description = "最低价格")
	private BigDecimal minPrice;

	/**
	 * 最高价格
	 */
	@Schema(description = "最高价格")
	private BigDecimal maxPrice;

	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;

	/**
	 * 审核操作状态【0：待审核 1：审核通过(商家可变更) -1：拒绝(商家可变更) 】
	 */
	@Schema(description = "草稿状态【-10：未发布；0：待审核 1：审核通过(商家可变更) -1：拒绝(商家可变更) 】")
	private Integer status;

	/**
	 * 简要描述,卖点等
	 */
	@Schema(description = "简要描述,卖点等")
	private String brief;

	/**
	 * 详细描述(手机端)
	 */
	@Schema(description = "详细描述(手机端)")
	private String contentM;

	/**
	 * 详细描述
	 */
	@Schema(description = "详细描述")
	private String content;

	/**
	 * 库存警告
	 */
	@Schema(description = "库存警告")
	private Integer stocksArm;

	/**
	 * 商品类型，E.普通商品，V:虚拟商品
	 */
	@Schema(description = "商品类型，E.普通商品，V:虚拟商品")
	private String prodType;

	/**
	 * 关键字
	 */
	@Schema(description = "关键字")
	private String keyWord;

	/**
	 * 参数属性
	 */
	@Schema(description = "参数属性")
	private String parameter;

	/**
	 * 用户自定义参数属性
	 */
	@Schema(description = "用户自定义参数属性")
	private String userParameter;

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
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 */
	@Schema(description = "库存计数方式，0：拍下减库存，1：付款减库存")
	private Boolean stockCounting;

	/**
	 * 商品搜索主图片
	 */
	@Schema(description = "商品搜索主图片")
	private String pic;

	/**
	 * 商品图片集合
	 */
	@Schema(description = "商品图片集合")
	private String imgPath;

	/**
	 * 视频
	 */
	@Schema(description = "视频")
	private String video;

	/**
	 * 运费模板id
	 */
	@Schema(description = "运费模板id")
	private Long transId;

	/**
	 * 主规格id
	 */
	@Schema(description = "主规格id")
	private Long mainSpecificationId;

	/**
	 * 规格属性
	 */
	@Schema(description = "规格属性")
	private String specification;

	/**
	 * 版本号
	 */
	@Schema(description = "版本号")
	private Integer version;

	/**
	 * 是否多规格(0单规格，1多规格)
	 */
	@Schema(description = "是否多规格(0单规格，1多规格)")
	private Boolean multipleSpecificationFlag;

	/**
	 * 是否限购(null:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)
	 */
	@Schema(description = "是否限购(null:无限购，O:每单限购，D:每日限购，W:每周限购，M:每月限购，Y:每年限购，A:终身限购)")
	private String quotaType;

	/**
	 * 限购数量
	 */
	@Schema(description = "限购数量")
	private Integer quotaCount;

	/**
	 * 限购时间
	 */
	@Schema(description = "限购时间")
	private Date quotaTime;


}
