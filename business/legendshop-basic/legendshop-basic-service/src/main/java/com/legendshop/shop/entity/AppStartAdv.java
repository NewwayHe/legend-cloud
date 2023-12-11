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
 * App启动广告表(AppStartAdv)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_app_start_adv")
public class AppStartAdv implements GenericEntity<Long> {

	private static final long serialVersionUID = -11722738740247890L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "APP_START_ADV_SEQ")
	private Long id;


	/**
	 * 广告名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 广告图片
	 */
	@Column(name = "img_url")
	private String imgUrl;


	/**
	 * 跳转的url
	 */
	@Column(name = "url")
	private String url;


	/**
	 * 广告的描述
	 */
	@Column(name = "description")
	private String description;


	/**
	 * 状态 0:下线,1:上线
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

}
