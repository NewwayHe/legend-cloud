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
 * @author legendshop
 */
@Data
@Schema(description = "短信模板配置")
public class SmsTemplateConfig {

	@Schema(description = "短信模板Code")
	private String templateCode;

	@Schema(description = "短信模板文本")
	private String templateText;

}
