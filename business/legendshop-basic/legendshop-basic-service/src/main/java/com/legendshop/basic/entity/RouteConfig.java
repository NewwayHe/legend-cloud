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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 网关路由配置
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_route_config")
public class RouteConfig extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 790454978277115003L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ROUTE_CONFIG_SEQ")
	private Long id;


	/**
	 * 路由id
	 */
	@Column(name = "route_id")
	private String routeId;


	/**
	 * 路由名称
	 */
	@Column(name = "route_name")
	private String routeName;


	/**
	 * 断言信息
	 */
	@Column(name = "predicates")
	private String predicates;


	/**
	 * 过滤器信息
	 */
	@Column(name = "filters")
	private String filters;

	/**
	 * 路由地址
	 */
	@Column(name = "uri")
	private String uri;

	/**
	 * 顺序
	 */
	@Column(name = "seq")
	private Integer seq;

}
