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
 * @Date: 2020-07-17 16:21
 */
@Data
public class PrintCloudReq {

	/**
	 * 业务类型（默认：getPrintImg）
	 */
	private String method;
	/**
	 * 快递100分配给贵司的的授权key
	 */
	private String key;
	/**
	 * 加密签名信息：MD5(param+t+key+secret)；加密后字符串转大写
	 */
	private String sign;
	/**
	 * 当前请求时间戳
	 */
	private String t;
	/**
	 * 其他参数组合成的json对象
	 */
	private PrintCloudParam param;
}
