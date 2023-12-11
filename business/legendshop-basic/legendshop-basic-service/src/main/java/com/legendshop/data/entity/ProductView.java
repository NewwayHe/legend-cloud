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

import java.util.Date;

/**
 * 商品访问记录(ProductView)实体类
 *
 * @author legendshop
 * @since 2021-03-24 17:20:23
 */
@Data
@Entity
@Table(name = "ls_product_view")
public class ProductView implements GenericEntity<Long> {

	private static final long serialVersionUID = -50827086501329928L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "productView_SEQ")
	private Long id;

	/**
	 * 商家id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 商家名称
	 */
	@Column(name = "shop_name")
	private String shopName;

	/**
	 * 产品id
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 产品名称
	 */
	@Column(name = "product_name")
	private String productName;

	/**
	 * 访问总人数
	 */
	@Column(name = "view_people")
	private Integer viewPeople;

	/**
	 * 访问总次数
	 */
	@Column(name = "view_frequency")
	private Integer viewFrequency;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 加入购物车数量
	 */
	@Column(name = "cart_num")
	private Integer cartNum;

	/**
	 * 商品收藏数
	 */
	@Column(name = "favorite_num")
	private Integer favoriteNum;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

}
