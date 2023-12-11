/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.response;

import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-14 16:14
 */
@Data
public class QueryTrackRouteInfo {
	/**
	 * 出发位置
	 */
	private QueryTrackPosition from;
	/**
	 * 当前位置
	 */
	private QueryTrackPosition cur;
	/**
	 * 收货地
	 */
	private QueryTrackPosition to;
}
