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
 * 商品收藏表(Favorite)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_favorite_product")
public class FavoriteProduct implements GenericEntity<Long> {

	private static final long serialVersionUID = -16354091432798017L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "FAVORITE_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 收藏时间
	 */
	@Column(name = "addtime")
	private Date addtime;


	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

}
