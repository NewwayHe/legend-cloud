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
 * @Date: 2020-07-16 13:41
 */
@Data
public class SubscribeParam {

	/**
	 * 订阅的快递公司的编码，一律用小写字母
	 */
	private String company;
	/**
	 * 订阅的快递单号，单号的最大长度是32个字符
	 */
	private String number;
	/**
	 * 出发地城市，省-市-区，非必填，填了有助于提升签收状态的判断的准确率，请尽量提供
	 */
	private String from;
	/**
	 * 目的地城市，省-市-区，非必填，填了有助于提升签收状态的判断的准确率，且到达目的地后会加大监控频率，请尽量提供
	 */
	private String to;
	/**
	 * 我方分配给贵司的的授权key
	 */
	private String key;
	/**
	 * 附加参数信息
	 */
	private SubscribeParameters parameters;
}
