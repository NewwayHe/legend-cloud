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

import java.util.List;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-09-17 14:24
 */
@Data
public class BOrderQueryDataInfo {
	/**
	 * 业务服务类型
	 */
	private List<String> serviceType;
	/**
	 * 支持的快递公司编码
	 */
	private String kuaidiCom;

}
