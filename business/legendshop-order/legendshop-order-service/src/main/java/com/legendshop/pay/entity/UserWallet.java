/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * (UserWallet)实体类
 *
 * @author legendshop
 * @since 2021-03-13 14:09:47
 */
@Data
@Entity
@Table(name = "ls_user_wallet")
public class UserWallet extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -91077777590062744L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "userWallet_SEQ")
	private Long id;

	/**
	 * 用户Id，单表唯一
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 支付密码
	 */
	@Column(name = "pay_password")
	private String payPassword;

	/**
	 * 累计金额
	 */
	@Column(name = "cumulative_amount")
	private BigDecimal cumulativeAmount;

	/**
	 * 累计支出
	 */
	@Column(name = "cumulative_expenditure")
	private BigDecimal cumulativeExpenditure;

	/**
	 * 当前可用金额
	 */
	@Column(name = "available_amount")
	private BigDecimal availableAmount;

	/**
	 * 冻结金额
	 */
	@Column(name = "frozen_amount")
	private BigDecimal frozenAmount;

	/**
	 * 钱包状态
	 */
	@Column(name = "state")
	private Integer state;

}
