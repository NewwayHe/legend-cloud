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
import lombok.Data;

import java.util.Date;

/**
 * 支付单日志表【主要记录支付单异常记录，协助分析用户支付行为，以防支异常或攻击】(PaySettlementLog)实体类
 *
 * @author legendshop
 * @since 2022-04-28 11:48:11
 */
@Data
@Entity
@Table(name = "ls_pay_settlement_log")
public class PaySettlementLog implements GenericEntity<Long> {

	private static final long serialVersionUID = -25662697090514197L;

	/**
	 * 主健ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "paySettlementLog_SEQ")
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
	 * 支付方式编号
	 */
	@Column(name = "pay_type_id")
	private String payTypeId;

	/**
	 * 日志内容
	 */
	@Column(name = "info")
	private String info;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
