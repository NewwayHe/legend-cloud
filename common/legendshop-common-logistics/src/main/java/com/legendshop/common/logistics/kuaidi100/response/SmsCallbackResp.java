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
 * 短信回调结果
 *
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-21 10:02
 */
@Data
public class SmsCallbackResp {
	/**
	 * 状态值：0-表示失败；1-表示成功
	 */
	private Integer status;
	/**
	 * 返回数据信息：短信发送状态
	 */
	private SmsCallbackData data;
	/**
	 * 信息内容：返回具体的信息体描述
	 */
	private String msg;
	/**
	 * 附加信息
	 */
	private String extra;
	/**
	 * 该值表示所数据的总条数
	 */
	private Integer total;

}
