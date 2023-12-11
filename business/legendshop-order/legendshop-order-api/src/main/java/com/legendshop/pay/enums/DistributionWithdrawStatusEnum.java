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
 * @author legendshop
 * @version 1.0.0
 * @title DistributionWithdrawStatusEnum
 * @date 2022/3/30 17:49
 * @description： 分销钱包支付状态
 */
@Getter
@AllArgsConstructor
public enum DistributionWithdrawStatusEnum {

	FAIL(-10, "支付失败"),
	UNPAID(0, "未支付"),
	SUCCESS(10, "支付成功");

	final Integer value;
	final String desc;
}
