/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.product.dto.ActivitySkuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 单品SKU表(Sku)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "单品SKU表DTO")
public class SkuBO implements Serializable {

	private static final long serialVersionUID = 5424950311863514132L;
	/**
	 * 单品ID
	 */
	@Schema(description = "单品ID")
	private Long id;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 销售属性组合（中文）
	 */
	@Schema(description = "销售属性组合（中文）")
	private String cnProperties;


	/**
	 * sku的销售属性组合字符串（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,格式是p1:v1;p2:v2
	 */
	@Schema(description = " sku的销售属性组合字符串")
	private String properties;


	/**
	 * 用户自定义的销售属性，key:value 格式
	 */
	@Schema(description = "用户自定义的销售属性")
	private String userProperties;


	/**
	 * 原价
	 */
	@Schema(description = "原价")
	private BigDecimal originalPrice;

	/**
	 * 销售价格
	 */
	@Schema(description = "销售价格")
	private BigDecimal price;

	/**
	 * 成本价
	 */
	@Schema(description = "成本价")
	private BigDecimal costPrice;

	@Schema(description = "活动相关信息")
	private ActivitySkuDTO activitySkuDTO;

	/**
	 * 销售库存（商品在付款减库存的状态下，该sku上未付款的订单数量）
	 */
	@Schema(description = "销售库存")
	private Integer stocks;

	/**
	 * 活动总库存
	 */
	@Schema(description = "活动总库存")
	private Integer activityStocks;

	/**
	 * 实际库存
	 */
	@Schema(description = "实际库存")
	private Integer actualStocks;

	/**
	 * 库存预警
	 */
	@Schema(description = "库存预警")
	private Integer stocksArm;

	/**
	 * 已经销售数量
	 */
	@Schema(description = "已经销售数量")
	private Integer buys;

	/**
	 * SKU名称
	 */
	@Schema(description = "SKU名称")
	private String name;


	/**
	 * 商品描述
	 */
	@Schema(description = "商品描述")
	private String brief;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * sku状态。 1:正常 ；0:删除
	 */
	@Schema(description = " sku状态。 1:正常 ；0:删除")
	private Integer status;


	/**
	 * sku级别发货时间
	 */
	@Schema(description = "sku级别发货时间")
	private Date skuDeliveryTime;


	/**
	 * 商家设置的外部id
	 */
	@Schema(description = "商家设置的外部id")
	private String outerId;


	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private Date updateTime;


	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	private Date createTime;


	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;


	/**
	 * 商品条形码
	 */
	@Schema(description = "商品条形码")
	private String modelId;


	/**
	 * sku图片
	 */
	@Schema(description = "sku图片")
	private String pic;


	/**
	 * 物流体积
	 */
	@Schema(description = "物流体积")
	private Double volume;


	/**
	 * 物流重量(千克)
	 */
	@Schema(description = "物流重量(千克)")
	private Double weight;


	/**
	 * sku营销活动类型
	 * {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	@Schema(description = "sku营销活动类型NULL:没有参加营销活动")
	private String skuType;

	/**
	 * 对应属性值ID
	 */
	@Schema(description = "对应属性值ID")
	private String propValueIds;

	/**
	 * 是否收藏，0没收藏，非0为收藏id
	 */
	@Schema(description = " 是否收藏，0没收藏，非0为收藏id")
	private Long favouriteFlag;

	/**
	 *
	 */
	@Schema(description = "减免金额")
	private BigDecimal reduce;

	/**
	 * 减免折扣
	 */
	@Schema(description = "减免折扣")
	private BigDecimal off;

	/**
	 * 限时折扣id
	 */
	@Schema(description = "限时折扣id")
	private Long marketingLimitDiscountsProductId;

	/**
	 * SKU标题
	 */
	@Schema(description = "SKU标题")
	private List<KeyValueEntityDTO> propertiesNameList;

	@Schema(description = "拼团价格")
	private BigDecimal mergePrice;

	@Schema(description = "团购价格")
	private BigDecimal groupPrice;

	@Schema(description = "是否已勾选")
	private Boolean check = false;

	@Schema(description = "是否禁选[同一时间段一个sku只能参与一个满减满折活动]")
	private Boolean disable = false;

	/**
	 * 方便前端处理
	 */
	@Schema(description = "skuId")
	private Long skuId;

	@Schema(description = "是否被选了积分商品")
	private Boolean integralFlag = Boolean.FALSE;

	@Schema(description = "是否被选了积分抵扣商品")
	private Boolean integralDeductionFlag = Boolean.FALSE;

	@Schema(description = "是否被选了满减满折商品")
	private Boolean marketingRewardFlag = Boolean.FALSE;

	@Schema(description = "是否被选了限时折扣商品")
	private Boolean marketingLimitDiscountsFlag = Boolean.FALSE;
	@Schema(description = "兑换积分")
	private Integer costIntegral;

	@Schema(description = "积分库存")
	private Integer integralStocks;

	@Schema(description = "暂时调整比例")
	private DistributionAdjustmentBO adjustmentBO;

	@Schema(description = "自购省金额")
	private BigDecimal selfPurchaseAmount;

	@Schema(description = "商品佣金")
	private BigDecimal commission;

	@Schema(description = "会员价")
	private BigDecimal memberAmount;

	@Schema(description = "限时折扣价")
	private BigDecimal discountPrice;

	@Schema(description = "预估收益")
	private BigDecimal estimatedIncome;

	@Schema(description = "评论总数")
	private Integer comments;
	/**
	 * 是否包邮
	 */
	@Schema(description = "是否包邮")
	private Boolean freeFlag = Boolean.TRUE;

	@Schema(description = "运费")
	private BigDecimal freightPrice = BigDecimal.ZERO;

	public Long getSkuId() {
		return id;
	}
}
