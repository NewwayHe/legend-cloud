/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信短连接(WechatScene)DTO
 *
 * @author legendshop
 * @since 2021-03-16 15:08:44
 */
@Data
@Schema(description = "微信短连接DTO")
public class WechatSceneDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -35668150485923561L;

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
