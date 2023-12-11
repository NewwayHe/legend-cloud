/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信配置基类
 *
 * @author legendshop
 */
@Data
public class WxConfig implements ParamsConfig {

	private static final long serialVersionUID = -7972601812471355515L;
	@JsonProperty(value = "appid")
	private String appId;

	@JsonProperty(value = "secret")
	private String secret;

}
