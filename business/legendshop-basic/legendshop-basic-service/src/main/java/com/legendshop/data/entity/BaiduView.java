/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 百度（移动）统计记录(BaiduView)实体类
 *
 * @author legendshop
 * @since 2021-06-19 13:54:05
 */
@Data
@Entity
@Table(name = "ls_baidu_view")
public class BaiduView implements GenericEntity<Long> {

	private static final long serialVersionUID = 972635229849742162L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "baiduView_SEQ")
	private Long id;

	/**
	 * h5访问总人数
	 */
	@Column(name = "h5_uv")
	private Integer h5Uv;

	/**
	 * h5访问总次数
	 */
	@Column(name = "h5_pv")
	private Integer h5Pv;

	/**
	 * 小程序访问总人数
	 */
	@Column(name = "mini_uv")
	private Integer miniUv;

	/**
	 * 小程序访问总次数
	 */
	@Column(name = "mini_pv")
	private Integer miniPv;

	/**
	 * 访问总人数
	 */
	@Column(name = "total_uv")
	private Integer totalUv;

	/**
	 * 访问总次数
	 */
	@Column(name = "total_pv")
	private Integer totalPv;

	/**
	 * 归档时间
	 */
	@Column(name = "archive_time")
	private String archiveTime;

}
