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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格区间(PriceRange)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_price_range")
public class PriceRange implements GenericEntity<Long> {

	private static final long serialVersionUID = 868166681010936019L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRICE_RANGE_SEQ")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 起始价格
	 */
	@Column(name = "start_price")
	private BigDecimal startPrice;


	/**
	 * 结束价格
	 */
	@Column(name = "end_price")
	private BigDecimal endPrice;


	/**
	 * 类型：0 表示 商品价格区间；1 表示 订单价格区间
	 */
	@Column(name = "type")
	private Integer type;


	/**
	 * 创建时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

}
