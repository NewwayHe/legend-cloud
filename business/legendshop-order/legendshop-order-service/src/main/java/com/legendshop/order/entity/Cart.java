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

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车(Cart)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_cart")
public class Cart implements GenericEntity<Long> {

	private static final long serialVersionUID = 920053626291416366L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "CART_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 产品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * SkuID
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * sku价格
	 */
	@Column(name = "price")
	private BigDecimal price;


	/**
	 * 购物车产品个数
	 */
	@Column(name = "total_count")
	private Integer totalCount;


	/**
	 * 购物时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 选中状态 1:选中 0：未选中
	 */
	@Column(name = "select_flag")
	private Boolean selectFlag;

	/**
	 * 物料URL
	 */
	@Column(name = "material_url")
	private String materialUrl;

}
