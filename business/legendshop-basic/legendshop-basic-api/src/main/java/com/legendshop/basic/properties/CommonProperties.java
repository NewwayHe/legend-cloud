/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * common.yml 配置文件
 *
 * @author legendshop
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "legendshop.common")
public class CommonProperties {

	/**
	 * 后端服务域名
	 */
	@Schema(description = "后端服务域名")
	private String serviceDomainName;

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
	 * 商家管理PC端前端域名
	 */
	@Schema(description = "商家管理PC端前端域名")
	private String shopPcDomainName;

	/**
	 * 后端管理PC端前端域名
	 */
	@Schema(description = "平台管理PC端前端域名")
	private String adminPcDomainName;

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
