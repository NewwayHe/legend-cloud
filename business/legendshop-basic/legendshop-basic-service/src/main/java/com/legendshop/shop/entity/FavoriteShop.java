/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 店铺收藏表(FavoriteShop)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_favorite_shop")
public class FavoriteShop implements GenericEntity<Long> {

	private static final long serialVersionUID = 512420391283313251L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "FAVORITE_SHOP_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 收藏时间
	 */
	@Column(name = "rec_date")
	private Date recDate;


}
