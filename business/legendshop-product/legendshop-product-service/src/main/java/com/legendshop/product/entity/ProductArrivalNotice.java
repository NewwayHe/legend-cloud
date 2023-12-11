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
 * 商品到货通知表(ProdArrivalInform)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_arrival_notice")
public class ProductArrivalNotice implements GenericEntity<Long> {

	private static final long serialVersionUID = -84051347946566035L;

	/**
	 * 到货通知记录表ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PRODUCT_ARRIVAL_NOTICE_SEQ")
	private Long id;


	/**
	 * 消费者用户id
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 商家id
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 商品id
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * skuId
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * 手机
	 */
	@Column(name = "mobile_phone")
	private String mobilePhone;


	/**
	 * 状态 0:尚未通知用户 1:已通知用户
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
