/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 物流服务配置类,默认是快递100
 *
 * @author legendshop
 */
@Data
public class LogisticsProperties implements Serializable {

	private static final long serialVersionUID = -262693110881492154L;
	/**
	 * 授权key
	 */
	private String appKey;

	/**
	 * 授权秘钥
	 */
	private String appSecret;

	/**
	 * customer
	 */
	private String customer;

	/**
	 * userid
	 */
	private String userid;

	/**
	 * 物流回调接口
	 */
	private String logisticsCallbackUrl;

	/**
	 * 退货物流回调接口
	 */
	private String refundCallbackUrl;

}
