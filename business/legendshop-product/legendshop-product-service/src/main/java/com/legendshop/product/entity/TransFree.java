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
 * 条件包邮(TransFree)实体类
 *
 * @author legendshop
 * @since 2020-09-04 16:54:49
 */
@Data
@Entity
@Table(name = "ls_trans_free")
public class TransFree implements GenericEntity<Long> {

	private static final long serialVersionUID = 233801547282894367L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "transFree_SEQ")
	private Long id;

	/**
	 * 模板id
	 */
	@Column(name = "trans_id")
	private Long transId;

	/**
	 * 满件包邮
	 */
	@Column(name = "num")
	private Long num;

	/**
	 * 满多少金额包邮
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 1：满件 2：满金额
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 记录时间
	 */
	@Column(name = "rec_date")
	private Date recDate;
}
