/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 行业目录(IndustryDirectory)实体类
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
@Data
@Entity
@Table(name = "ls_industry_directory")
public class IndustryDirectory implements GenericEntity<Long> {

	private static final long serialVersionUID = -59586954443263320L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "industryDirectory_SEQ")
	private Long id;

	/**
	 * 目录名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 排序
	 */
	@Column(name = "seq")
	private BigDecimal seq;


	/**
	 * 状态
	 */
	@Column(name = "state")
	private Boolean state;


	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
