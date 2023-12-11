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
import com.legendshop.basic.enums.LocationGradeEnum;
import lombok.Data;

/**
 * 地区表(Location)实体类
 *
 * @author legendshop
 * @since 2020-10-14 18:10:09
 */
@Data
@Entity
@Table(name = "ls_location")
public class Location implements GenericEntity<Long> {

	private static final long serialVersionUID = 510723946599061684L;

	/**
	 * 地区id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "location_SEQ")
	private Long id;

	/**
	 * 地区编码
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 地区名
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 父级id
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 地区级别 1：省级 2：市级 3：区级 4：街道 {@link LocationGradeEnum}
	 */
	@Column(name = "grade")
	private Integer grade;

}
