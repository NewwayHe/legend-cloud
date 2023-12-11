/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 站内信载体
 *
 * @author legendshop
 */
@Data
public class SiteMessageInfoDTO implements Serializable {

	private static final long serialVersionUID = -2067683867437353140L;
	/**
	 * 发送者
	 */
	private final String sendName;

	/**
	 * 接收者
	 */
	private final String receiveName;

	/**
	 * 标题
	 */
	private final String title;

	/**
	 * 内容
	 */
	private final String text;
}
