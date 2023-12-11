/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (ShopView)实体类
 *
 * @author legendshop
 * @since 2021-06-17 13:43:40
 */
@Data
@Entity
@Table(name = "ls_shop_out_stock_rate")
public class ShopOutStockRate implements GenericEntity<Long> {

	private static final long serialVersionUID = 777476965736916078L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "shopOutStockRate_SEQ")
	private Long id;

	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Integer shopId;

	/**
	 * 缺货率
	 */
	@Column(name = "out_stock_rate")
	private BigDecimal outStockRate;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;
}
