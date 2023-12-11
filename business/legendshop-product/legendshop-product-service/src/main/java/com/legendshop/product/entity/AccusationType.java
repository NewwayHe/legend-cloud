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
 * 举报类型(AccusationType)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_accusation_type")
public class AccusationType extends BaseEntity implements GenericEntity<Long> {


	private static final long serialVersionUID = 5403839415831173092L;
	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ACCUSATION_SEQ")
	@Column(name = "id")
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 状态:[0下线,1上线]
	 */
	@Column(name = "status")
	private Integer status;


}
