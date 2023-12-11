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
 * 用户钱包详情中间表(UserWalletDetailedCentre)实体类
 *
 * @author legendshop
 * @since 2022-04-27 13:56:50
 */
@Data
@Entity
@Table(name = "ls_user_wallet_detailed_centre")
public class UserWalletDetailedCentre extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 309982687149701114L;

	/**
	 * 主键id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "userWalletDetailedCentre_SEQ")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 业务id(提现:提现明细id, 佣金:佣金明细id )
	 */
	@Column(name = "business_id")
	private Long businessId;

	/**
	 * 业务类型（佣金结算，提现）
	 */
	@Column(name = "business_type")
	private WalletBusinessTypeEnum businessType;

	/**
	 * 操作类型（收入，支出）
	 * WalletOperationTypeEnum
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
	 * 状态（0: 不处理，10: 未处理（未通知），20: 未处理（已通知），30: 已处理）
	 * WalletCentreStatusEnum
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 操作备注
	 */
	@Column(name = "remarks")
	private String remarks;


}
