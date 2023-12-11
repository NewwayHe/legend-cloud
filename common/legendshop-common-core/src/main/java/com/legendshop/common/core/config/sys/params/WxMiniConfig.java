/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class WxMiniConfig extends WxConfig {


	private static final long serialVersionUID = -8524780887115809514L;

	/**
	 * 是否开启微信敏感词过渡
	 */
	private Boolean wxSecCheck;

	/**
	 * 需要过滤的敏感图片后缀
	 */
	private String wxImageSuffix;

	/**
	 * 微信小程序获取tokenUrl
	 */
	private String wxMiniTokenUrl;

	/**
	 * 获取微信小程序二维码token
	 */
	private String wxMiniCodeUrl;
}
