/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.response.cloud;

import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-10-27 16:00
 */
@Data
public class COrderRespData {
	/**
	 * 任务id
	 */
	private String taskId;
	/**
	 * 快递100返回给您的平台订单id
	 */
	private String orderId;

}
