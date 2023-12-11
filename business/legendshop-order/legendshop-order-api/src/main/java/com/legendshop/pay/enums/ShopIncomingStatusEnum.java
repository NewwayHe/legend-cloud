/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 进件状态
 * 申请状态 NOT_APPLIED:未申请 REVIEWING:申请审核中 REVIEW_BACK:申请已驳回 AGREEMENT_SIGNING:协议待签署 BUSINESS_OPENING:业务开通中 COMPLETED:申请已完成
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum ShopIncomingStatusEnum {

	/**
	 *
	 */
	NOT_APPLIED("NOT_APPLIED", "未申请"),

	REVIEWING("REVIEWING", "申请审核中"),

	REVIEW_BACK("REVIEW_BACK", "申请已驳回"),

	AGREEMENT_SIGNING("AGREEMENT_SIGNING", "协议待签署"),

	BUSINESS_OPENING("BUSINESS_OPENING", "业务开通中"),

	COMPLETED("COMPLETED", "申请已完成"),

	SUBMITTING("SUBMITTING", "提交中");

	private String value;

	private String desc;

}
