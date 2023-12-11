/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 百度移动统计设置DTO
 *
 * @author legendshop
 * @create: 2021-06-18 14:11
 */
@Data
public class BaiDuMobileStatisticsSettingDTO implements Serializable {

	private static final long serialVersionUID = 8738172603267335150L;

	private String accessToken;

	private String refreshToken;

	private String apiKey;

	private String secretKey;
}
