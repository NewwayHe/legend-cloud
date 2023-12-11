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
 * 微信短连接(WechatScene)实体类
 *
 * @author legendshop
 * @since 2021-03-16 15:08:42
 */
@Data
@Entity
@Table(name = "ls_wechat_scene")
public class WechatScene implements GenericEntity<Long> {

	private static final long serialVersionUID = -53081129246037449L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "wechatScene_SEQ")
	private Long id;

	/**
	 * 原始数据
	 */
	@Column(name = "scene")
	private String scene;

	/**
	 * 跳转页面
	 */
	@Column(name = "page")
	private String page;

	/**
	 * md5后的数据
	 */
	@Column(name = "md5")
	private String md5;

	/**
	 * base64后的数据
	 */
	@Column(name = "base64")
	private String base64;

}
