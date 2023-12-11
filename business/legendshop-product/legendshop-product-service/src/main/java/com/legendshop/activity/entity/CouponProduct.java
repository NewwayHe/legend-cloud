/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 优惠券商品表(CouponProd)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_coupon_product")
public class CouponProduct implements GenericEntity<Long> {

	private static final long serialVersionUID = -80099151448797064L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "COUPON_PRODUCT_SEQ")
	private Long id;


	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id")
	private Long couponId;


	/**
	 * 商品id
	 */
	@Column(name = "sku_id")
	private Long skuId;

}
