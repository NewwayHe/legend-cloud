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
 * 敏感字过滤表(SensWord)实体类
 *
 * @author legendshop
 * @since 2021-12-22 14:58:46
 */
@Data
@Entity
@Table(name = "ls_sens_word")
public class SensWord extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 632279691022340041L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SENSWORD_SEQ")
	private Long id;

	/**
	 * 关键字
	 */
	@Column(name = "words")
	private String words;

	/**
	 * 是否全局敏感字
	 */
	@Column(name = "is_global")
	private Integer isGlobal;

	/**
	 * 创建人
	 */
	@Column(name = "creator")
	private String creator;
}
