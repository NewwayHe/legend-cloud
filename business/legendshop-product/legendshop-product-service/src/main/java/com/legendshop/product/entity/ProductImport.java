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

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_import")
public class ProductImport implements GenericEntity<Long> {

	private static final long serialVersionUID = 1114583702073063935L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "productImport_SEQ")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Integer shopId;

	/**
	 * 总条数
	 */
	@Column(name = "count")
	private Integer count;

	/**
	 * 成功条数
	 */
	@Column(name = "success")
	private Integer success;

	/**
	 * 错误条数
	 */
	@Column(name = "fail")
	private Integer fail;

	/**
	 * 操作人
	 */
	@Column(name = "operator")
	private String operator;

	/**
	 * 操作时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
