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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * (ExceptionLog)实体类
 *
 * @author legendshop
 * @since 2020-09-25 10:20:07
 */
@Data
@Entity
@Table(name = "ls_exception_log")
public class ExceptionLog extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -65415685605393747L;

	/**
	 * 主键Id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "EXCEPTION_LOG_SEQ")
	private Long id;

	/**
	 * 记录类型
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 等级
	 */
	@Column(name = "level")
	private Integer level;

	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;

	/**
	 * 关键字
	 */
	@Column(name = "keyword")
	private String keyword;

	/**
	 * 错误描述
	 */
	@Column(name = "description")
	private String description;

}
