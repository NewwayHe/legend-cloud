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
 * @Date: 2020-07-17 19:31
 */
@Data
public class PrintHtmlData {
	/**
	 * html代码
	 */
	private List<String> template;
	/**
	 * 快递单号
	 */
	private String kuaidinum;
	/**
	 * 大头笔
	 */
	private String bulkpen;
	/**
	 * 电子面单链接
	 */
	private List<String> templateurl;
	/**
	 * 子单号
	 */
	private String childNum;
}
