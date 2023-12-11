/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * APP启动广告
 *
 * @author legendshop
 */
@Schema(description = "启动广告详情")
@Data
public class AppStartAdvOnlineDTO implements Serializable {

	/**
	 * 唯一ID
	 */
	@Schema(description = "启动广告唯一ID")
	private Long id;

	/**
	 * 图片地址
	 */
	@Schema(description = "图片地址")
	private String photo;

	/**
	 * 广告跳转URL
	 */
	@Schema(description = "广告跳转动作")
	private AppDecorateActionDTO action;

}
