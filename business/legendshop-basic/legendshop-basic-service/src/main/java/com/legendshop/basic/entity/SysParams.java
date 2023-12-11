/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * (SysParams)实体类
 *
 * @author legendshop
 * @since 2020-08-28 12:00:45
 */
@Data
@Entity
@Table(name = "ls_sys_params")
public class SysParams implements GenericEntity<Long> {

	private static final long serialVersionUID = 857463286733098988L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "sysParams_SEQ")
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "des")
	private String des;

	/**
	 * 类型
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 分组
	 */
	@Column(name = "group_by")
	private String groupBy;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@Column(name = "update_time")
	private Date updateTime;


	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

}
