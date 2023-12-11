/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台资金流水(PlateCapitalFlow)BO
 *
 * @author legendshop
 * @since 2020-09-18 17:26:13
 */
@Data
public class PlateCapitalFlowBO implements Serializable {

	private static final long serialVersionUID = -22432629155101853L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 支付流水号
	 */
	private String paySerialNumber;

	/**
	 * 收支类型
	 */
	private String flowType;

	/**
	 * 交易类型
	 */
	private String dealType;

	/**
	 * 支付方式
	 */
	private String payType;


	/**
	 * 支付方式名称
	 */
	private String payTypeName;

	/**
	 * 金额
	 */
	private BigDecimal amount;

	/**
	 * 记录时间
	 */
	private Date recDate;

	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * 备注
	 */
	private String remark;

}
