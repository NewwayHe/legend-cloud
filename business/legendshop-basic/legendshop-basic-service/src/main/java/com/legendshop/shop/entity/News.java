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
 * 新闻(News)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_news")
public class News extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 412657722148613036L;

	/**
	 * 新闻ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "NEWS_SEQ")
	@Column(name = "id")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "admin_user_id")
	private Long userId;


	/**
	 * 新闻分类
	 */
	@Column(name = "news_category_id")
	private Long newsCategoryId;


	/**
	 * 新闻标题
	 */
	@Column(name = "news_title")
	private String newsTitle;


	/**
	 * 新闻提要，保存是截取新闻前100个字
	 */
	@Column(name = "news_brief")
	private String newsBrief;


	/**
	 * 新闻内容
	 */
	@Column(name = "news_content")
	private String newsContent;


	/**
	 * 新闻状态，1：上线，0：下线
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 是否高亮,1:yes,0:no
	 */
	@Column(name = "high_line")
	private Integer highLine;

	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;

	/**
	 * 类型 1：内链  2外链
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 路径
	 */
	@Column(name = "url")
	private String url;


}
