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

/**
 * 银行编码(BankCode)实体类
 *
 * @author legendshop
 * @since 2021-04-07 09:56:28
 */
@Data
@Entity
@Table(name = "ls_bank_code")
public class BankCode implements GenericEntity<Long> {

	private static final long serialVersionUID = -78789441534052150L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "bankCode_SEQ")
	private Long id;

	/**
	 * 总行名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 总行简称
	 */
	@Column(name = "abbreviation")
	private String abbreviation;

	/**
	 * 总行编码
	 */
	@Column(name = "code")
	private String code;

}
