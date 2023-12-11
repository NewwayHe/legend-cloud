/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class SimulatePayNotifyDTO extends PayNotifyDTO {

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 加密时间
	 */
	private Long validTime;

}
