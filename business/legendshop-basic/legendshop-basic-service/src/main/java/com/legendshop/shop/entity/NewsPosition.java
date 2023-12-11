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
 * 新闻位置表(NewsPosition)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_news_position")
public class NewsPosition implements GenericEntity<Long> {

	private static final long serialVersionUID = -83268008750984930L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "NEWS_POSITION_SEQ")
	private Long id;


	/**
	 * 新闻Id
	 */
	@Column(name = "news")
	private Long news;


	/**
	 * 新闻位置[底部、头部、...]
	 */
	@Column(name = "position")
	private Integer position;


	/**
	 * 排序
	 */
	@Column(name = "seq")
	private Integer seq;

}
