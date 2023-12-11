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
 * @Date: 2020-07-14 16:13
 */
@Data
public class QueryTrackData {
	/**
	 * 时间，原始格式
	 */
	private String time;
	/**
	 * 物流轨迹节点内容
	 */
	private String context;
	/**
	 * 格式化后时间
	 */
	private String ftime;
	/**
	 * 行政区域的编码
	 */
	private String areaCode;
	/**
	 * 行政区域的名称
	 */
	private String areaName;
	/**
	 * 签收状态 (0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投)
	 */
	private String status;
}
