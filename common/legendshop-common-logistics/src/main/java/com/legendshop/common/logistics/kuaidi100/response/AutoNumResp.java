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
 * @Date: 2020-07-17 10:04
 */
@Data
public class AutoNumResp {
	/**
	 * 请忽略
	 */
	public String lengthPre;
	/**
	 * 快递公司对应的编码
	 */
	private String comCode;
	/**
	 * 请忽略
	 */
	private String noPre;
	/**
	 * 请忽略
	 */
	private String noCount;
}
