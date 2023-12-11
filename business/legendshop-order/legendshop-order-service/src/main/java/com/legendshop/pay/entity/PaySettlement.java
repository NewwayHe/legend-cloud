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

import java.util.Date;

/**
 * 支付单据(orderSettlement)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_pay_settlement")
public class PaySettlement extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 966846379982753299L;

	/**
	 * 支付结算单据ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PAY_SETTLEMENT_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 支付单号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;


	/**
	 * 支付单类型
	 */
	@Column(name = "use_type")
	private String useType;


	/**
	 * 支付来源
	 */
	@Column(name = "pay_source")
	private String paySource;


	/**
	 * 支付单状态
	 */
	@Column(name = "state")
	private Integer state;


	/**
	 * 清算时间
	 */
	@Column(name = "clearing_time")
	private Date clearingTime;
}
