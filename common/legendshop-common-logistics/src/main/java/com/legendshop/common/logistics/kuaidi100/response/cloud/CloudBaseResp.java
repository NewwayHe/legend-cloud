/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.response.cloud;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-10-27 15:58
 */
public class CloudBaseResp<T> {
	/**
	 * 响应码
	 */
	private String code;
	/**
	 * 响应结果描述
	 */
	private String message;
	/**
	 * 响应数据
	 */
	private T data;
}
