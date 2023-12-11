/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "后台待处理事项数量BO")
@Builder
public class PendingMattersAdminBO {

	/**
	 * 待审核店铺
	 */
	@Schema(description = "待审核店铺")
	private Integer AuditedShopCount;

	/**
	 * 待审核品牌
	 */
	@Schema(description = "待审核品牌")
	private Integer AuditedBrandCount;

	/**
	 * 待审核商品
	 */
	@Schema(description = "待审核商品")
	private Integer AuditedProductCount;

	/**
	 * 待审核举报商品
	 */
	@Schema(description = "待审核举报商品")
	private Integer AuditedReportProductCount;

	/**
	 * 待审核商品评论
	 */
	@Schema(description = "待审核商品评论")
	private Integer AuditedProductCommentCount;
	/**
	 * 待审核售后
	 */
	@Schema(description = "待审核售后")
	private Integer AuditedAfterSaleCount;

	/**
	 * 待处理账单结算
	 */
	@Schema(description = "待处理账单结算")
	private Integer AuditedBillingCount;

	/**
	 * 待处理余额提现
	 */
	@Schema(description = "待处理余额提现")
	private Integer AuditedBalanceWithdrawCount;

	/**
	 * 待处理意见反馈
	 */
	@Schema(description = "待处理意见反馈")
	private Integer AuditedFeedbackCount;
}
