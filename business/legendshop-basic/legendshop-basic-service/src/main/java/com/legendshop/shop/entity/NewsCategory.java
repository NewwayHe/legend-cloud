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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 新闻栏目(NewsCat)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_news_cat")
public class NewsCategory extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -86600780963343295L;

	/**
	 * 新闻栏目ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "NEWS_CAT_SEQ")
	private Long id;


	/**
	 * 新闻栏目名称
	 */
	@Column(name = "news_category_name")
	private String newsCategoryName;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;


	/**
	 * 显示页面  {@link com.legendshop.basic.enums.DisplayPageEnum}
	 */
	@Column(name = "display_page")
	private Integer displayPage;

}
