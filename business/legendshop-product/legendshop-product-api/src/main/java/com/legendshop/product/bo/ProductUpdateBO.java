/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.product.dto.*;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品(Prod)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品BO")
public class ProductUpdateBO implements Serializable {

	private static final long serialVersionUID = 6923442164160198914L;

	/**
	 * 产品ID
	 */
	@Schema(description = "产品ID")
	private Long id;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 商家用户ID
	 */
	@Schema(description = "商家用户ID")
	private Long shopUserId;

	/**
	 * 规格样式 {@link com.legendshop.product.enums.ProductSpecificationStyle}
	 */
	@Schema(description = "规格样式 PIC：图片样式；TXT：文本样式")
	private String specificationStyle;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String name;

	/**
	 * 销售价范围
	 */
	@Schema(description = "销售价范围")
	private String price;

	/**
	 * 简要描述,卖点等
	 */
	@Schema(description = "简要描述,卖点等")
	private String brief;

	/**
	 * (全局商城)分类
	 */
	@Schema(description = "(全局商城)分类")
	private List<Long> globalCategoryId;

	/**
	 * (全局商城)一级分类名
	 */
	@Schema(description = "(全局商城)一级分类名")
	private String globalFirstCatName;

	/**
	 * (全局商城)二级分类名
	 */
	@Schema(description = "(全局商城)二级分类名")
	private String globalSecondCatName;

	/**
	 * (全局商城)三级分类名
	 */
	@Schema(description = "(全局商城)三级分类名")
	private String globalThirdCatName;

	/**
	 * 商家小分类数组
	 */
	@Schema(description = "商家小分类数组")
	private List<Long> shopCategoryId;

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
	 * 库存量
	 */
	@Schema(description = "库存量")
	private Integer stocks;

	/**
	 * sku数量
	 */
	@Schema(description = "sku数量")
	private Integer skuCount;


	/**
	 * 品牌名称
	 **/
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 品牌logo
	 **/
	@Schema(description = "品牌品牌logo")
	private String brandPic;

	/**
	 * 商品视频
	 */
	@Schema(description = "商品视频")
	private String video;


	/**
	 * 是否预售商品
	 */
	@Schema(description = "是否预售商品")
	private Boolean preSellFlag;

	@Schema(description = "预售结束时间")
	private Date preSaleEnd;

	/**
	 * 店铺信息的BO对象
	 */
	@Schema(description = "店铺信息的BO对象")
	private ShopDetailBO shopDetailBO;


	/**
	 * 商品图片集合
	 **/
	@Schema(description = "商品图片集合")
	private List<String> productPics;

	/**
	 * 商品sku集合
	 */
	@Schema(description = "商品sku集合")
	private List<SkuBO> sku;

	/**
	 * 商品sku集合长度
	 */
	@Schema(description = "商品sku集合长度")
	private Integer skuBOListLength;

	/**
	 * 商品规格集合
	 **/
	@Schema(description = "商品规格集合")
	private List<ProductPropertyDTO> prodPropDtoList;

	/**
	 * 商品主规格
	 **/
	@Schema(description = "商品主规格")
	private ProductPropertyDTO productPropertyDTO;

	@Schema(description = "商品参数集合")
	private List<ProductPropertyBO> paramBOList;

	/**
	 * 商品参数组集合
	 **/
	@Schema(description = "商品参数组集合")
	private List<ProductPropertyBO> paramGroupBOList;

	/**
	 * 库存警告
	 */
	@Schema(description = "库存警告")
	private Integer stocksArm;

	/**
	 * 商品类型，{@link com.legendshop.product.enums.ProductTypeEnum }
	 */
	@Schema(description = "商品类型: E.实物商品、V:虚拟商品")
	private String prodType;

	/**
	 * 商品动态参数
	 */
	@Schema(description = "商品动态参数")
	private String parameter;

	/**
	 * 用户自定义参数
	 */
	@Schema(description = "用户自定义参数")
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
	 * 品牌
	 */
	@Schema(description = "品牌")
	private Long brandId;

	/**
	 * 实际库存
	 */
	@Schema(description = "实际库存")
	private Integer actualStocks;

	/**
	 * 评论得分
	 */
	@Schema(description = "评论得分")
	private Integer reviewScores;

	/**
	 * 运费模板ID
	 */
	@Schema(description = "运费模板ID")
	private Long transId;

	/**
	 * 运费模板名称
	 */
	@Schema(description = "运费模板名称")
	private String transName;

	/**
	 * 库存计数方式，0：拍下减库存，1：付款减库存
	 */
	@Schema(description = "库存计数方式，0：拍下减库存，1：付款减库存")
	private Boolean stockCounting;

	/**
	 * 微信小程序码
	 */
	@Schema(description = "微信小程序码")
	private String wxACode;

	/**
	 * 商家端微信小程序码
	 */
	@Schema(description = "商家端微信小程序码")
	private String shopWxCode;

	/**
	 * 图片路径
	 */
	@Schema(description = "图片路径")
	private String imgPath;

	/**
	 * 图片路径
	 */
	@Schema(description = "商品主图")
	private List<String> img;

	/**
	 * 商品主图
	 */
	@Schema(description = "商品主图")
	private String pic;

	/**
	 * 商品上架方式
	 * {@link com.legendshop.product.enums.ProductOnSaleWayEnum}
	 */
	@Schema(description = "商品上架方式：商品上架方式 -1：不上架，0：预约上架，1：审核通过后马上上架")
	private Integer onSaleWay;

	/**
	 * 预约上架时间
	 */
	@Schema(description = "预约上架时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date appointTime;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 预约上架id
	 */
	@Schema(description = "预约上架id")
	private Long appointId;

	/**
	 * 上架标识：是否定时执行了上架操作
	 */
	@Schema(description = "上架标识：是否定时执行了上架操作")
	private Boolean onSellFlag;


	/**
	 * 预售信息
	 */
	@Schema(description = "预售信息")
	private PreSellProductDTO preSellProductDTO;

	/**
	 * 好评数量
	 */
	@Schema(description = "好评数量")
	private String goodCommentsPercent;

	@Schema(description = "skuId")
	private Long skuId;

	@Schema(description = "skuType")
	private String skuType;

	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String siteName;

	/**
	 * 商品sku集合 的json格式字符串
	 */
	@Schema(description = "商品sku集合 的json格式字符串")
	private String skuBoListJson = "";

	/**
	 * 属性图片list(json格式),用于商品详情页 展示
	 */
	@Schema(description = "属性图片list(json格式),用于商品详情页 展示")
	private String propValueImgListJson;

	/**
	 * 商品属性图片
	 */
	@Schema(description = "商品属性图片")
	List<ProductPropertyImageDTO> propValueImgList;

	@Schema(description = "setUpTime")
	private String setUpTime;

	@Schema(description = "选择天数")
	private String selectDays;

	@Schema(description = "选择小时")
	private String selectHours;

	@Schema(description = "选择分钟")
	private String selectMinutes;

	@Schema(description = "设定商品发布后状态 1：上线，2：设定：有记录开始时间，0：放入仓库")
	private Integer publishStatus;

	/**
	 * 用户自定义属性
	 **/
	@Schema(description = "用户属性集合")
	private String userPropList;

	/**
	 * 查取 用户自定义 属性值
	 **/
	@Schema(description = "查取 用户自定义 属性值")
	private String valueAliasList;

	/**
	 * 选中商品属性名
	 */
	@Schema(description = "选中商品属性名")
	private List<String> propNameList;

	/**
	 * 商品属性图片
	 */
	@Schema(description = "商品属性图片")
	List<ProductPropertyImageDTO> propImageDtoList;

	/**
	 * 售后说明
	 **/
	@Schema(description = "售后说明 ")
	private String afterSaleName;

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
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;

	/**
	 * 观看人数，同一个IP半个小时之类只算一次
	 */
	@Schema(description = "观看人数")
	private Integer views;

	/**
	 * 已经销售数量
	 */
	@Schema(description = "已经销售数量")
	private Integer buys;

	/**
	 * 评论数
	 */
	@Schema(description = "评论数")
	private Integer comments;

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

	@Schema(description = "草稿状态，-10: 商品未发布、-1: 拒绝、0: 待审核、1: 通过")
	private Integer draftStatus;

	/**
	 * 店铺状态
	 * {@link ShopDetailStatusEnum}
	 */
	@Schema(description = "店铺状态")
	private Integer shopStatus;

	/**
	 * 主规格id
	 */
	@Schema(description = "主规格id")
	private Long mainSpecificationId;


	@Schema(description = "商品规格")
	private String specification;


	@Schema(description = "商品规格")
	private List<ProductPropertyDTO> specificationList;

	@Schema(description = "版本号")
	private Integer version;

	@Schema(description = "是否被收藏")
	private Boolean collectionFlag;

	@Schema(description = "活动可选")
	private Boolean activitySelectFlag;

	@Schema(description = "非法活动sku")
	private List<SkuBO> illegalSkuBOList;

	@Schema(description = "最大销售价")
	private BigDecimal maxPrice;

	@Schema(description = "最小销售价")
	private BigDecimal minPrice;

	// 分佣佣金 ===============================

	/**
	 * 自购开关
	 */
	@Schema(description = "自购开关")
	private String selfPurchasedSwitch;

	@Schema(description = "自购省金额")
	private String selfPurchaseAmount;

	@Schema(description = "商品佣金")
	private String commission;


	@Schema(description = "推广佣金比例")
	private String ratio;

	@Schema(description = "成本价")
	private String costPrice;

	@Schema(description = "是否参与分销 0:否, 1:是")
	private Integer distributionFlag;
	// 佣金比例 ===============================

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 商品SKU 营销信息
	 **/
	@Schema(description = "商品SKU 营销信息")
	private List<ActivitySkuDTO> activitySkuList;

	@Schema(description = "是否选中")
	private Boolean selected = Boolean.FALSE;

	@Schema(description = "分享文案")
	private String distributionPosterSuggestions;

	@Schema(description = "限购对象")
	private ProductQuotaDTO productQuotaDTO;
	/**
	 * 商品咨询信息
	 */
	@Schema(description = "商品咨询信息")
	private List<ProductConsultDTO> productConsultList;

	@Schema(description = "审核备注")
	private String auditOpinion;

	public List<SkuBO> getIllegalSkuBOList() {
		if (ObjectUtil.isNull(illegalSkuBOList)) {
			return new ArrayList<>();
		}
		return illegalSkuBOList;
	}

	public Boolean getActivitySelectFlag() {
		return !CollUtil.isEmpty(this.sku);
	}


	public Integer getSkuBOListLength() {
		return CollUtil.isNotEmpty(getSku()) ? getSku().size() : 0;
	}

	public String getRemarks() {
		if (CollUtil.isEmpty(getSku())) {
			if (CollUtil.isEmpty(getIllegalSkuBOList())) {
				return "-";
			}
			return composeRemarks(getIllegalSkuBOList());
		}
		return composeRemarks(this.sku);
	}

	@Transient
	public Long getPreSaleEndTimestamp() {
		return preSaleEnd == null ? null : preSaleEnd.getTime();
	}

	public String composeRemarks(List<SkuBO> skuBOList) {
		StringBuffer sb = new StringBuffer("共有" + skuBOList.size() + "个sku");
		Integer integralDeductionCount = 0;
		Integer integralCount = 0;
		Integer marketingLimitDiscountsCount = 0;
		Integer marketingRewardCount = 0;
		Integer activitySku = 0;
		for (SkuBO skuBO : skuBOList) {
			boolean activitySkuFlag = false;
			if (skuBO.getIntegralDeductionFlag()) {
				integralDeductionCount++;
				activitySkuFlag = true;
			}
			if (skuBO.getIntegralFlag()) {
				integralCount++;
				activitySkuFlag = true;
			}
			if (skuBO.getMarketingLimitDiscountsFlag()) {
				marketingLimitDiscountsCount++;
				activitySkuFlag = true;
			}
			if (skuBO.getMarketingRewardFlag()) {
				marketingRewardCount++;
				activitySkuFlag = true;
			}
			if (activitySkuFlag) {
				activitySku++;
			}
		}
		if (activitySku == 0) {
			return sb.toString();
		}
		sb.append("，其中");
		sb.append(activitySku + "个SKU已参与活动");
		if (integralDeductionCount > 0) {
			sb.append(",");
			sb.append(integralDeductionCount + "个SKU已参与积分抵扣活动");
		}
		if (integralCount > 0) {
			sb.append(",");
			sb.append(integralCount + "个SKU已参与积分兑换活动");
		}
		if (marketingLimitDiscountsCount > 0) {
			sb.append(",");
			sb.append(marketingLimitDiscountsCount + "个SKU已参与限时折扣活动");
		}
		if (marketingRewardCount > 0) {
			sb.append(",");
			sb.append(marketingRewardCount + "个SKU已参与满减满折活动");
		}
		return sb.toString();
	}
}
