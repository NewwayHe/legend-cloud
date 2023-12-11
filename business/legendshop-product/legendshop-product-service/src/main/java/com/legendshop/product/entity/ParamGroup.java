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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 参数组表(ParamGroup)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_param_group")
public class ParamGroup extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 136871375761161170L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PARAM_GROUP_SEQ")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 副标题
	 */
	@Column(name = "memo")
	private String memo;

	/**
	 * 规格来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Column(name = "source")
	private String source;

}
