/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 优惠券退还规则设置，勾选的规则点击保存后生效；
 * 1、提交订单未付款可退还，即用户提交订单却未完成付款时，订单变为失效状态时，返还订单中所有使用了的优惠券；
 * 2、生成订单申请退款可退还，即用户提交订单并付款成功后申请全订单退款，完成退款流程后，返还订单中所有使用了的优惠券；
 * 3、生成订单全部商品申请售后可退还，即用户提交订单并付款成功后申请了全订单退货/退款，完成售后流程后，返还订单中所有使用了的优惠券；
 *
 * @author legendshop
 */
@Data
public class CouponRefundSettingConfig implements Serializable {

	private static final long serialVersionUID = 1497672652259960939L;

	/**
	 * 提交订单未付款可退还(首字母不能大写)
	 */
	private Boolean non_PAYMENT_REFUNDABLE;

	/**
	 * 生成订单申请退款可退还(首字母不能大写)
	 */
	private Boolean payment_REFUNDABLE;


	/**
	 * 生成订单全部商品申请售后可退还(首字母不能大写)
	 */
	private Boolean payment_ALL_AFTER_SALES_REFUNDABLE;
}
