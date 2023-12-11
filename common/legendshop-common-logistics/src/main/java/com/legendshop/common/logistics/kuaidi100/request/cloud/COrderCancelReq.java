/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.request.cloud;

import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-11-25 15:47
 */
@Data
public class COrderCancelReq extends CloudBaseReq {
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 订单ID
	 */
	private String orderId;
	/**
	 * 取消原因，例：暂时不寄件了
	 */
	private String cancelMsg;
}
