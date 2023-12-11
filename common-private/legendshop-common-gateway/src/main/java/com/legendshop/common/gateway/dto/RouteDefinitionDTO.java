/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.gateway.dto;

import lombok.Data;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @author legendshop
 */
@Data
public class RouteDefinitionDTO extends RouteDefinition {

	/**
	 * 路由名称
	 */
	private String routeName;
}
