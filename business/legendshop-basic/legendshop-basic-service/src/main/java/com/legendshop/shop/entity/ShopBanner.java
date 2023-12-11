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

/**
 * 商家默认的轮换图表(ShopBanner)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_banner")
public class ShopBanner implements GenericEntity<Long> {

	private static final long serialVersionUID = 565873967149976969L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_BANNER_SEQ")
	private Long id;


	@Column(name = "shop_id")
	private Long shopId;


	@Column(name = "image_file")
	private String imageFile;


	@Column(name = "url")
	private String url;


	@Column(name = "seq")
	private Integer seq;

}
