/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

/**
 * 网关路由配置DTO
 *
 * @author legendshop
 */
@Data
public class RouteConfigDTO extends BaseDTO {

	private static final long serialVersionUID = 790454978277115003L;


	/**
	 * 路由id
	 */
	private String routeId;


	/**
	 * 路由名称
	 */
	private String routeName;


	/**
	 * 断言信息
	 */
	private String predicates;


	/**
	 * 过滤器信息
	 */
	private String filters;

	/**
	 * 路由地址
	 */
	private String uri;

	/**
	 * 顺序
	 */
	private Integer seq;

}
