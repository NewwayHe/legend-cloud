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
 * @Date: 2020-08-28 14:24
 */
@Data
public class SubscribePushData {
	/**
	 * 物流轨迹节点内容
	 */
	private String context;
	/**
	 * 时间，原始格式
	 */
	private String time;
	/**
	 * 格式化后时间
	 */
	private String ftime;
	/**
	 * 本数据元对应的签收状态。只有在开通签收状态服务（见上面"status"后的说明）且在订阅接口中提交resultv2标记后才会出现
	 */
	private String status;
	/**
	 * 本数据元对应的行政区域的编码，只有在开通签收状态服务（见上面"status"后的说明）且在订阅接口中提交resultv2标记后才会出现
	 */
	private String areaCode;
	/**
	 * 本数据元对应的行政区域的名称，开通签收状态服务（见上面"status"后的说明）且在订阅接口中提交resultv2标记后才会出现
	 */
	private String areaName;
}
