/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (UserWallet)BO
 *
 * @author legendshop
 * @since 2021-03-13 14:09:49
 */
@Data
public class UserWalletBO implements Serializable {

	private static final long serialVersionUID = 401090359458013074L;

	private Long id;

	/**
	 * 用户Id，单表唯一
	 */
	@Schema(description = "用户Id，单表唯一")
	private Long userId;

	/**
	 * 支付密码
	 */
	@Schema(description = "支付密码")
	private String payPassword;

	/**
	 * 累计金额
	 */
	@Schema(description = "累计金额")
	private BigDecimal cumulativeAmount;

	/**
	 * 当前可用金额
	 */
	@Schema(description = "当前可用金额")
	private BigDecimal availableAmount;

	/**
	 * 冻结金额
	 */
	@Schema(description = "冻结金额")
	private BigDecimal frozenAmount;

	/**
	 * 创建时间（初始化）
	 */
	@Schema(description = "创建时间（初始化）")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

}
