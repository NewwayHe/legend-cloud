/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.legendshop.product.enums.ProductDelStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品搜索DTO
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "商品搜索参数")
public class ProductQuery extends PageParams {


	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	/**
	 * 优惠券ID
	 */
	@Schema(description = "优惠券ID")
	private Long couponId;

	/**
	 * 满减满折活动id
	 */
	@Schema(description = "满减满折活动id")
	private Long marketRewardId;

	@Schema(description = "是否需要获取活动下的商品")
	private Boolean isObtainActivityProd = false;

	/**
	 * 限时折扣活动id
	 */
	@Schema(description = "限时折扣活动id")
	private Long marketLimitId;

	/**
	 * productID列表
	 */
	@Schema(description = "productID列表")
	private List<Long> productIdList;

	/**
	 * skuID列表
	 */
	@Schema(description = "skuID列表")
	private List<Long> skuIdList;

	/**
	 * (全局商城) 分类
	 */
	@Schema(description = "(全局商城) 分类")
	private Long categoryId;


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
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String name;


	/**
	 * 原价
	 */
	@DecimalMin(value = "0.00", inclusive = false, message = "原价不能小于0")
	@Digits(integer = 6, fraction = 2, message = "原价 保留2位小数且不能大于6位数")
	@Schema(description = "原价")
	private BigDecimal price;


	/**
	 * 销售价
	 */
	@Schema(description = "销售价")
	@DecimalMin(value = "0.00", inclusive = false, message = "销售价不能小于0")
	@Digits(integer = 6, fraction = 2, message = "销售价保留2位小数且不能大于6位数")
	private BigDecimal cash;


	/**
	 * 是否支持分销
	 */
	@Schema(description = "是否支持分销")
	private Integer supportDist;

	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "-10：未发布; 0：下架、1：上架、3：全部")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核操作状态：1通过、0待审核、-1不通过 2：违规下架 3: 违规锁定 4: 违规全部 5: 全部非违规商品")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Schema(description = "删除操作状态: -2：永久删除；-1：删除；1：正常")
	private Integer delStatus;


	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "开始时间")
	private Date startDate;


	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "结束时间")
	private Date endDate;


	/**
	 * 商品类型
	 * {@link com.legendshop.product.enums.ProductTypeEnum}
	 */
	@Schema(description = "商品类型，E.实物商品、V:虚拟商品")
	private String prodType;


	/**
	 * 品牌id
	 */
	@Schema(description = "品牌ID")
	private Long brandId;


	/**
	 * 有没发票
	 */
	@Schema(description = "有没发票")
	private Integer hasInvoice;


	/**
	 * 货到付款; 0:普通商品 , 1:货到付款商品
	 */
	@Schema(description = "货到付款; 0:普通商品 , 1:货到付款商品")
	private Integer supportCodStatus;


	/**
	 * 活动Id [秒杀活动ID]
	 */
	@Schema(description = "活动Id [秒杀活动ID]")
	private Integer activeId;


	/**
	 * 是否参加团购
	 */
	@Schema(description = "是否参加团购")
	private Boolean groupFlag;

	/**
	 * 关键字
	 */
	@Schema(description = "关键字")
	private String keyword;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String siteName;

	private Integer amount;

	/**
	 * 举报类型id
	 */
	@Schema(description = "举报类型id")
	private Long accusationTypeId;

	/**
	 * 举报状态  0:未处理， 1:已经处理
	 */
	@Schema(description = "举报状态  0:未处理， 1:已经处理")
	private Integer accStatus;

	/**
	 * 举报处理结果  -1:无效举报 1：有效举报  -2：恶意举报
	 */
	@Schema(description = "举报处理结果  -1:无效举报 1：有效举报  -2：恶意举报")
	private Integer accResult;

	@Schema(description = "活动开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityBegTime;

	@Schema(description = "活动结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date activityEndTime;

	/**
	 * 排序的字段
	 */
	@Schema(description = "排序的字段")
	private String prop;

	/**
	 * 排序的方向：asc 或者 desc
	 */
	@Schema(description = "排序的方向：asc 或者 desc")
	private String order;

	@Schema(description = "false 降序   true 升序")
	private Boolean descending;


	@Schema(description = "积分商品id")
	private Long integralProductId;

	/**
	 * 是否预售商品
	 */
	@Schema(description = "是否预售商品")
	private Boolean preSellFlag;

	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Schema(description = "支付方式,0:全额,1:定金")
	private Integer payPctType;

	@Schema(description = "是否参与分销 0:否, 1:是")
	private Integer distributionFlag;


	public void setDescending(Boolean descending) {
		this.descending = descending;
		if (null != descending) {
			if (descending) {
				order = "desc";
			} else {
				order = "asc";
			}
		}
	}
}
