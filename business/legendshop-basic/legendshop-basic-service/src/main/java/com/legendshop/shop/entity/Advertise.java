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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * (Advertise)实体类
 *
 * @author legendshop
 * @since 2022-04-27 15:23:36
 */
@Data
@Entity
@Table(name = "ls_advertise")
public class Advertise implements GenericEntity<Long> {

	private static final long serialVersionUID = 893353813588359483L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "advertise_SEQ")
	private Long id;

	/**
	 * 广告标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 投放广告用户类型
	 */
	@Column(name = "advertise_uesr_type")
	private String advertiseUesrType;

	/**
	 * 渠道：pc端,移动端
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 投放页面:首页,个人中心
	 */
	@Column(name = "advertise_page")
	private String advertisePage;

	/**
	 * 投放频率
	 */
	@Column(name = "advertise_frequency")
	private String advertiseFrequency;

	/**
	 * 链接
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 广告图片
	 */
	@Column(name = "photos")
	private String photos;

	/**
	 * 权重
	 */
	@Column(name = "seq")
	private Integer seq;

	/**
	 * 广告状态:0未开始,1开始,2暂停,3结束
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 投放开始时间
	 */
	@Column(name = "start_time")
	private Date startTime;

	/**
	 * 投放结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;

	/**
	 * 点击限制:1点击弹窗后不显示(已登陆),2点击弹窗后仍会显示
	 */
	@Column(name = "click_limit")
	private Integer clickLimit;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 广告次数
	 */
	@Schema(description = "广告次数")
	private Integer count;

}
