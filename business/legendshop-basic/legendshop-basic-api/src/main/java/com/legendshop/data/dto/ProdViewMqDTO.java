/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户浏览记录mq消息dto
 *
 * @author legendshop
 */
@Data
public class ProdViewMqDTO implements Serializable {

	private static final long serialVersionUID = 2481532258724412321L;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 商品浏览记录dto
	 */
	private ProductViewDTO productViewDTO;

}
