/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付宝提现DTO
 *
 * @author legendshop
 * 2021/10/26 10:59
 **/
@Data
public class AliWithdrawDTO implements Serializable {

	private String app_id;

	/**
	 * 公钥
	 */
	private String alipay_public_key;

	/**
	 * 私钥
	 */
	private String private_key;

	/**
	 * 商户转账唯一订单号
	 */
	private String out_biz_no;

	/**
	 * 收款方账户类型。可取值支付宝登录号
	 */
	private String payee_type;

	/**
	 * 收款方账户
	 */
	private String payee_account;

	/**
	 * 转账金额，单位：元。 只支持2位小数，小数点前最大支持13位，金额必须大于等于0.1元。
	 */
	private Double amount;

	/**
	 * 收款方真实姓名
	 */
	private String payee_real_name;

	/**
	 * 付款方姓名
	 */
	private String payer_show_name;

	/**
	 * 备注
	 */
	private String remark;


}
