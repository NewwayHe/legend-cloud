/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.swagger.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static com.legendshop.common.swagger.properties.SwaggerProperties.PREFIX;

/**
 * @author legendshop
 */
@Data
@ConfigurationProperties(PREFIX)
public class SwaggerProperties {


	public static final String PREFIX = "legendshop.swagger";
	/**
	 * 是否开启，生产环境建议关闭
	 */
	private Boolean enabled = Boolean.TRUE;

	/**
	 * 扫包的路径
	 **/
	private String basePackage = "";

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 排除的url路径
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * 描述
	 **/
	private String description = "";

	/**
	 * 版本
	 **/
	private String version = "";

	/**
	 * 许可证
	 **/
	private String license = "";

	/**
	 * 许可证URL
	 **/
	private String licenseUrl = "";

	/**
	 * 服务条款URL
	 **/
	private String termsOfServiceUrl = "";

	/**
	 * host
	 */
	private String host;

	/**
	 * 联系人信息
	 */
	private Contact contact = new Contact();

	/**
	 * 全局统一鉴权配置.
	 */
	private Authorization authorization = new Authorization();

	@Data
	@NoArgsConstructor
	public static class Contact {

		/**
		 * 联系人
		 **/
		private String name = "";

		/**
		 * 联系人url
		 **/
		private String url = "";

		/**
		 * 联系人email
		 **/
		private String email = "";

	}

	@Data
	@NoArgsConstructor
	public static class Authorization {

		/**
		 * 鉴权策略ID，需要和SecurityReferences ID保持一致.
		 */
		private String name = "";

		/**
		 * 需要开启鉴权URL的正则.
		 */
		private String authRegex = "^.*$";

		/**
		 * 鉴权作用域列表.
		 */
		private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();

		/**
		 * The token url list.
		 */
		private List<String> tokenUrlList = new ArrayList<>();
	}

	@Data
	@NoArgsConstructor
	public static class AuthorizationScope {

		/**
		 * 作用域名称.
		 */
		private String scope = "";

		/**
		 * 作用域描述.
		 */
		private String description = "";

	}
}
