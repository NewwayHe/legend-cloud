/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * common.yml 配置文件
 *
 * @author legendshop
 */
@Data
public class CommonPropertiesDTO {

	/**
	 * 用户Pc端前端域名
	 */
	@Schema(description = "用户Pc端前端域名")
	private String userPcDomainName;

	/**
	 * 用户移动端前端域名
	 */
	@Schema(description = "用户移动端前端域名")
	private String userMobileDomainName;

	/**
	 * 图片服务器地址
	 */
	@Schema(description = "图片服务器地址")
	private String photoServer;

	/**
	 * 静态资源服务器地址
	 */
	@Schema(description = "静态资源服务器地址")
	private String staticServer;
}
