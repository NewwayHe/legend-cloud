/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.monitor.model;

import lombok.Data;

/**
 * @author legendshop
 * @date 2020-09-16 14:08
 **/
@Data
public class ServiceNode {

	private String id;

	private Integer port;

	private String address;

	private String serviceName;

}
