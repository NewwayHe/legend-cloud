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
 * 固定运费(TransConstFee)实体类
 *
 * @author legendshop
 * @since 2020-09-07 14:43:45
 */
@Data
@Entity
@Table(name = "ls_trans_const_fee")
public class TransConstFee implements GenericEntity<Long> {

	private static final long serialVersionUID = 476917015986888882L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "transConstFee_SEQ")
	private Long id;

	/**
	 * 模板id
	 */
	@Column(name = "trans_id")
	private Integer transId;

	/**
	 * 固定运费
	 */
	@Column(name = "constant_price")
	private BigDecimal constantPrice;


	@Column(name = "rec_date")
	private Date recDate;

}
