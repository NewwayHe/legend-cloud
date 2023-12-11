/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 价格区间(PriceRange)实体类
 *
 * @author legendshop
 */
@Data
public class PriceRangeDTO implements Serializable {


	private static final long serialVersionUID = -8298573957933563022L;
	/**
	 * id
	 */
	private Long id;


	/**
	 * 店铺ID
	 */
	private Long shopId;


	/**
	 * 起始价格
	 */
	private BigDecimal startPrice;


	/**
	 * 结束价格
	 */
	private BigDecimal endPrice;


	/**
	 * 类型：0 表示 商品价格区间；1 表示 订单价格区间
	 */
	private Integer type;


	/**
	 * 创建时间
	 */
	private Date recDate;

}
