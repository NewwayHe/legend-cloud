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
 * 高德地图DTO
 *
 * @author legendshop
 */
@Data
public class MapDTO implements Serializable {

	private static final long serialVersionUID = 2108311401741756020L;

	/**
	 * apiKey
	 */
	private String key;
}
