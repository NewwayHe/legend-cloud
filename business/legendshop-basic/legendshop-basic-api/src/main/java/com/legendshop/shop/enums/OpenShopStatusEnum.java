/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商家入驻状态
 *
 * @author legendshop
 */
@AllArgsConstructor
@Getter
public enum OpenShopStatusEnum {

	/**
	 * 未提交入驻申请
	 */
	NOT_SUBMITTED(0),

	/**
	 * 审核通过
	 */
	APPROVED(1),

	/**
	 * 待审核
	 */
	REVIEW(2),

	/**
	 * 审核拒绝
	 */
	REFUSE(-1);

	/**
	 * The num.
	 */
	private final Integer status;
}
