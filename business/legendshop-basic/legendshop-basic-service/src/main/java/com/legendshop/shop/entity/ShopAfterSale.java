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
 * 售后服务说明表(ShopAfterSale)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_after_sale")
public class ShopAfterSale implements GenericEntity<Long> {

	private static final long serialVersionUID = 958631588652221485L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_AFTER_SALE_SEQ")
	private Long id;


	/**
	 * 用户名称
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * shopId
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;


	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 录入时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

}
