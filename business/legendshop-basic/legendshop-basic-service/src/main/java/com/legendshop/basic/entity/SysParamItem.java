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
 * (SysParamItem)实体类
 *
 * @author legendshop
 * @since 2020-08-28 14:17:38
 */
@Data
@Entity
@Table(name = "ls_sys_param_item")
public class SysParamItem implements GenericEntity<Long> {

	private static final long serialVersionUID = -15884739655222268L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "sysParamItem_SEQ")
	private Long id;


	@Column(name = "parent_id")
	private Long parentId;


	/**
	 * 描述
	 */
	@Column(name = "des")
	private String des;

	/**
	 * 键
	 */
	@Column(name = "key_word")
	private String keyWord;

	/**
	 * 值
	 */
	@Column(name = "value")
	private String value;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;


	@Column(name = "update_time")
	private Date updateTime;


	/**
	 * java类型
	 */
	@Column(name = "data_type")
	private String dataType;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
}
