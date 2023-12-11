/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 物流公司(LogisticsCompany)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_logistics_company")
public class LogisticsCompany implements GenericEntity<Long> {

	private static final long serialVersionUID = -59471836297256286L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "DELIVERY_SEQ")
	private Long id;


	/**
	 * 所属店铺
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 物流公司名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 物流公司官网URL
	 */
	@Column(name = "company_home_url")
	private String companyHomeUrl;


	/**
	 * 物流公司编号根据快递100查询
	 */
	@Column(name = "company_code")
	private String companyCode;


	/**
	 * 建立时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 修改时间
	 */
	@Column(name = "modify_time")
	private Date modifyTime;


	/**
	 * 被采用次数
	 */
	@Column(name = "use_count")
	private Integer useCount;

	/**
	 * 父类id
	 */
	@Column(name = "parent_id")
	private Long parentId;
}
