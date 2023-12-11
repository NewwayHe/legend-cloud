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
 * @Date: 2020-11-20 11:09
 */
@Data
public class COrderQueryReq extends CloudBaseReq {
	/**
	 *
	 */
	private String sendManPrintAddr;

	private String recManPrintAddr;

	private String address;
}
