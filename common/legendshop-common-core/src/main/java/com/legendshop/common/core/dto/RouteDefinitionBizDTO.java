/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import cn.hutool.json.JSONArray;
import lombok.Data;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由业务DTO
 *
 * @author legendshop
 */
@Data
public class RouteDefinitionBizDTO {

	/**
	 * 路由名称
	 */
	private String routeName;

	private String id;

	private JSONArray predicates;

	private JSONArray filters;

	private URI uri;

	private Map<String, Object> metadata = new HashMap();

	private int order = 0;
}
