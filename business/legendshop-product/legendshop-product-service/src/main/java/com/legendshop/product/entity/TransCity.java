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

/**
 * 每个城市的运费设置(TransCity)实体类
 *
 * @author legendshop
 * @since 2020-09-04 16:54:42
 */
@Data
@Entity
@Table(name = "ls_trans_city")
public class TransCity implements GenericEntity<Long> {

	private static final long serialVersionUID = -27306428205307710L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "transCity_SEQ")
	private Long id;

	/**
	 * 模板id
	 */
	@Column(name = "trans_id")
	private Long transId;

	/**
	 * 类型父id
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 城市id
	 */
	@Column(name = "city_id")
	private Long cityId;

	/**
	 * 类型1:区域限售 2：运费计算 3：条件包邮 {@link}
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 省编码
	 */
	@Column(name = "province_id")
	private Long provinceId;

	/**
	 * 省份选中状态
	 */
	@Column(name = "select_flag")
	private Boolean selectFlag;
}
