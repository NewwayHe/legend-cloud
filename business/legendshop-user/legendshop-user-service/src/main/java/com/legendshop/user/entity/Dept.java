/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 部门表(Dept)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_dept")
public class Dept extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -50790446260798092L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "DEPARTMENT_SEQ")
	private Long id;


	/**
	 * 部门名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;


	/**
	 * 联系人
	 */
	@Column(name = "contact")
	private String contact;


	/**
	 * 联系电话
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 父节点
	 */
	@Column(name = "parent_id")
	private Long parentId;

	@Transient
	private Boolean hasChildren;


}
