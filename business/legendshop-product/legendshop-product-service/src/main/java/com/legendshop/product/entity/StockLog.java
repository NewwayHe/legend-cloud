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
 * 库存历史表(StockLog)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_stock_log")
public class StockLog implements GenericEntity<Long> {

	private static final long serialVersionUID = -92847264428369003L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "STOCK_LOG_SEQ")
	private Long id;


	/**
	 * 商品Id
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * skuId
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * 商品名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 变更之前的库存
	 */
	@Column(name = "before_stock")
	private Integer beforeStock;


	/**
	 * 变更之后的库存
	 */
	@Column(name = "after_stock")
	private Integer afterStock;


	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;


	/**
	 * 更新备注
	 */
	@Column(name = "update_remark")
	private String updateRemark;

}
