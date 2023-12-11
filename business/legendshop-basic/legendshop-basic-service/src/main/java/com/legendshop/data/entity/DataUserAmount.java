/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 用户数量统计表(DataUserAmount)实体类
 *
 * @author legendshop
 * @since 2021-03-22 14:12:07
 */
@Data
@Entity
@Table(name = "ls_data_user_amount")
public class DataUserAmount implements GenericEntity<Long> {

	private static final long serialVersionUID = 991083258134923806L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "dataUserAmount_SEQ")
	private Long id;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 累计用户数量
	 */
	@Column(name = "people_amount")
	private Integer peopleAmount;

	/**
	 * 全渠道新增用户数量
	 */
	@Column(name = "people_new")
	private Integer peopleNew;

	/**
	 * PC端新增用户
	 */
	@Column(name = "pc_new")
	private Integer pcNew;

	/**
	 * app端新增用户
	 */
	@Column(name = "app_new")
	private Integer appNew;

	/**
	 * h5端新增用户
	 */
	@Column(name = "h5_new")
	private Integer h5New;

	/**
	 * 公众号端新增用户
	 */
	@Column(name = "mp_new")
	private Integer mpNew;

	/**
	 * 小程序端新增用户
	 */
	@Column(name = "mini_new")
	private Integer miniNew;

	/**
	 * 未知来源用户
	 */
	@Column(name = "unknown")
	private Integer unknown;

}
