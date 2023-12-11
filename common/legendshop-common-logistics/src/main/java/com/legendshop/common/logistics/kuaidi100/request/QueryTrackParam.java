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
 * @Date: 2020-07-14 15:58
 */
@Data
public class QueryTrackParam {
	/**
	 * 查询的快递公司的编码，一律用小写字母
	 */
	private String com;
	/**
	 * 查询的快递单号， 单号的最大长度是32个字符
	 */
	private String num;
	/**
	 * 收件人或寄件人的手机号或固话
	 */
	private String phone;
	/**
	 * 出发地城市，省-市-区
	 */
	private String from;
	/**
	 * 目的地城市，省-市-区
	 */
	private String to;
	/**
	 * 添加此字段表示开通行政区域解析功能。0：关闭（默认），1：开通行政区域解析功能，2：开通行政解析功能并且返回出发、目的及当前城市信息
	 */
	private String resultv2 = "0";
	/**
	 * 返回数据格式。0：json（默认），1：xml，2：html，3：text
	 */
	private String show = "0";
	/**
	 * 返回结果排序方式。desc：降序（默认），asc：升序
	 */
	private String order = "desc";
}
