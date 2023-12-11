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

/**
 * 业务配置表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_business_setting")
public class BusinessSetting implements GenericEntity<Long> {

	private static final long serialVersionUID = 630802421100148685L;

	/**
	 * 主键
	 */
	@GeneratedValue(strategy = GenerationType.ASSIGNED)
	@Id
	@TableGenerator(name = "generator", pkColumnValue = "CONST_TABLE_SEQ")
	@Column(name = "id")
	private Long id;


	/**
	 * 业务类型
	 * {@link com.legendshop.basic.enums.BusinessSettingTypeEnum}
	 */
	@Column(name = "type")
	private String type;


	/**
	 * 数据
	 */
	@Column(name = "value")
	private String value;


}
