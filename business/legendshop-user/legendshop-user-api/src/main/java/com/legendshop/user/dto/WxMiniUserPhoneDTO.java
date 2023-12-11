/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class WxMiniUserPhoneDTO {

	@Schema(description = "用户绑定的手机号（国外手机号会有区号）")
	private String phoneNumber;

	@Schema(description = "没有区号的手机号")
	private String purePhoneNumber;

	@Schema(description = "区号")
	private String countryCode;

}
