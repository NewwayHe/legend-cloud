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
 * 常用分类(CategoryComm)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_category_comm")
public class ShopCategoryComm implements GenericEntity<Long> {

	private static final long serialVersionUID = 339871056911602620L;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "CATEGORY_COMM_SEQ")
	private Long id;


	@Column(name = "shop_id")
	private Long shopId;


	@Column(name = "category_id")
	private Long categoryId;


	@Column(name = "rec_date")
	private Date recDate;

}
