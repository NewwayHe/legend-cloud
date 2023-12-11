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
 * 装修页面(AppPageManage)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_decorate_page")
public class DecoratePage implements GenericEntity<Long> {

	private static final long serialVersionUID = -16180902105104549L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "DECORATE_PAGE_SEQ")
	private Long id;


	/**
	 * 页面名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 页面类型 [INDEX：首页 POSTER：海报页] 参考枚举DecoratePageCategoryEnum
	 */
	@Column(name = "category")
	private String category;


	/**
	 * 状态 [-1：草稿 0:已修改未发布 1:已发布 ]参考枚举DecoratePageStatusEnum
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 是否已使用
	 */
	@Column(name = "use_flag")
	private Boolean useFlag;


	/**
	 * 可编辑的装修数据
	 */
	@Column(name = "data")
	private String data;


	/**
	 * 已发布的装修数据
	 */
	@Column(name = "release_data")
	private String releaseData;

	/**
	 * 来源[pc：pc端 mobile：移动端]参考枚举DecoratePageSourceEnum
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 封面图
	 */
	@Column(name = "cover_picture")
	private String coverPicture;


	/**
	 * 创建时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

	/**
	 * 类型   1 官方  2 原创
	 */
	@Column(name = "type")
	private Integer type;

}
