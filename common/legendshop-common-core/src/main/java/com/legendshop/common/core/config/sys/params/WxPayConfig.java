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
 * @author legendshop
 */
@Data
public class WxPayConfig extends WxConfig {


	private static final long serialVersionUID = 174714602199180784L;
	@ApiModelProperty(value = "商户ID")
	private String mchId;
	private String mchKey;
	private String token;
	private String partnerKey;
	private String refundFile;

	private String refundFlag;

	@ApiModelProperty(value = "是否启用支付")
	private Boolean enabled;

	@ApiModelProperty(value = "是否启用沙箱支付")
	private Boolean enabledSandbox;
}
