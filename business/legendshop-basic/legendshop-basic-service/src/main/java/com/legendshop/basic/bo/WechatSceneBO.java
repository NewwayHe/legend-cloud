/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信短连接(WechatScene)BO
 *
 * @author legendshop
 * @since 2021-03-16 15:08:44
 */
@Data
public class WechatSceneBO implements Serializable {

	private static final long serialVersionUID = -44392049632948091L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 原始数据
	 */
	@Schema(description = "原始数据")
	private String scene;

	/**
	 * 跳转页面
	 */
	@Schema(description = "跳转页面")
	private String page;

	/**
	 * md5后的数据
	 */
	@Schema(description = "md5后的数据")
	private String md5;

	/**
	 * base64后的数据
	 */
	@Schema(description = "base64后的数据")
	private String base64;

}
