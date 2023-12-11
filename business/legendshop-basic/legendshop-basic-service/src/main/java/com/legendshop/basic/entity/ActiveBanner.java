/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 活动的banner图(ActiveBanner)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_active_banner")
public class ActiveBanner implements GenericEntity<Long> {

	private static final long serialVersionUID = -94255560368226353L;

	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ACTIVE_BANNER_SEQ")
	private Long id;


	/**
	 * 图片路径
	 */
	@Column(name = "image_file")
	private String imageFile;


	/**
	 * 图片链接
	 */
	@Column(name = "url")
	private String url;


	/**
	 * 序号
	 */
	@Column(name = "seq")
	private Integer seq;


	/**
	 * banner图类型 ActiveBannerTypeEnum
	 */
	@Column(name = "banner_type")
	private String bannerType;

}
