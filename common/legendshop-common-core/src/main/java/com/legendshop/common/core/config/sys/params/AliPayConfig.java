/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付宝配置
 *
 * @author legendshop
 */
@Data
public class AliPayConfig implements ParamsConfig {

	private static final long serialVersionUID = -5249719787856788L;
	@ApiModelProperty(value = "是否启用")
	private Boolean enabled;

	private String sellerEmail;

	private String partner;

	private String key;

	@ApiModelProperty("付款方名字")
	private String payerShowName;

	@ApiModelProperty(value = "appId")
	private String appId;

	@ApiModelProperty(value = "密钥")
	private String rsaPrivate;

	@ApiModelProperty(value = "公钥")
	private String rsaPublish;

	/**
	 * 应用公钥证书路径，用于证书模式
	 */
	@ApiModelProperty(value = "应用公钥证书路径")
	private String merCertPath;

	/**
	 * 支付宝公钥证书路径，用于证书模式
	 */
	@ApiModelProperty(value = "支付宝公钥证书路径")
	private String aliCertPath;

	/**
	 * 支付宝根证书路径，用于证书模式
	 */
	@ApiModelProperty(value = "支付宝根证书路径")
	private String aliRootCertPath;


	private Boolean aliPayPc;

	private Boolean aliPayApp;

	private Boolean aliPayWeb;

	@ApiModelProperty(value = "回调路径")
	private String notifyUrl;
}
