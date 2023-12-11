/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.request;

import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-09-17 14:37
 */
@Data
public class BOrderGetCodeReq {
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 订单ID
	 */
	private String orderId;
}
