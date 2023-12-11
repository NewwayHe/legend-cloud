/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 运输费用(TransFee)实体类
 *
 * @author legendshop
 * @since 2020-09-04 16:54:46
 */
@Data
@Entity
@Table(name = "ls_trans_fee")
public class TransFee implements GenericEntity<Long> {

	private static final long serialVersionUID = 145055902506397023L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "transFee_SEQ")
	private Long id;

	/**
	 * 模板ID
	 */
	@Column(name = "trans_id")
	private Long transId;

	/**
	 * 续件运费
	 */
	@Column(name = "add_price")
	private BigDecimal addPrice;

	/**
	 * 首件运费
	 */
	@Column(name = "first_price")
	private BigDecimal firstPrice;

	/**
	 * 续件
	 */
	@Column(name = "add_num")
	private Double addNum;

	/**
	 * 首件
	 */
	@Column(name = "first_num")
	private Double firstNum;

	/**
	 * 1:件数、2:重量、3:体积
	 */
	@Column(name = "cal_freight_type")
	private String calFreightType;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 记录时间
	 */
	@Column(name = "rec_date")
	private Date recDate;
}
