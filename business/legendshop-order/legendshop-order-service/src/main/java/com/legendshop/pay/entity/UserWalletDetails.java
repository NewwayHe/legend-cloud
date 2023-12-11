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
import com.legendshop.pay.enums.UserWalletAmountTypeEnum;
import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户钱包收支记录详情(UserWalletDetails)实体类
 *
 * @author legendshop
 * @since 2021-03-13 14:44:00
 */
@Data
@Entity
@Table(name = "ls_user_wallet_details")
public class UserWalletDetails extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 917787306559828734L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "userWalletDetails_SEQ")
	private Long id;

	/**
	 * 用户Id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 流水号
	 */
	@Column(name = "serial_no")
	private Long serialNo;

	/**
	 * 钱包中间表ID
	 */
	@Column(name = "wallet_centre_id")
	private Long walletCentreId;

	/**
	 * 业务编码
	 */
	@Column(name = "business_id")
	private Long businessId;

	/**
	 * 业务类型
	 */
	@Column(name = "business_type")
	private WalletBusinessTypeEnum businessType;

	/**
	 * 操作类型
	 */
	@Column(name = "operation_type")
	private WalletOperationTypeEnum operationType;

	/**
	 * 金额类型(冻结金额，已结算金额)
	 * WalletAmountTypeEnum
	 */
	@Column(name = "amount_type")
	private UserWalletAmountTypeEnum amountType;

	/**
	 * 操作金额
	 */
	@Column(name = "amount")
	private BigDecimal amount;

	/**
	 * 操作金额
	 */
	@Column(name = "before_amount")
	private BigDecimal beforeAmount;

	/**
	 * 操作金额
	 */
	@Column(name = "after_amount")
	private BigDecimal afterAmount;

	/**
	 * 记录备注
	 */
	@Column(name = "remarks")
	private String remarks;

	/**
	 * 收支签名；MD5(时间+金额+用户ID+流水号)
	 */
	@Column(name = "signature")
	private String signature;

	/**
	 * 渠道来源（目前用于提现）
	 * VisitSourceEnum
	 */
	@Deprecated
	@Column(name = "source")
	private String source;

	/**
	 * 状态
	 * WalletDetailsStateEnum
	 */
	@Deprecated
	@Column(name = "state")
	private Integer state;

	/**
	 * 提现手续费，不是百分比
	 */
	@Deprecated
	@Column(name = "withdrawal_charge")
	private BigDecimal withdrawalCharge;

	/**
	 * 可用金额(不包括本次操作)
	 */
	@Deprecated
	@Column(name = "available_admount")
	private BigDecimal availableAdmount;

}
