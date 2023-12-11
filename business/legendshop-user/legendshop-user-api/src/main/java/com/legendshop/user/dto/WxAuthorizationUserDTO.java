/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.legendshop.common.core.enums.VisitSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxAuthorizationUserDTO {

	private String phoneNumber;

	private String openId;

	private String ip;

	private VisitSourceEnum source;

	/**
	 * 游客ID
	 */
	@Schema(description = "游客id")
	private String visitorId;

	public WxAuthorizationUserDTO(String phoneNumber, String openId, String ip, VisitSourceEnum source) {
		this.phoneNumber = phoneNumber;
		this.openId = openId;
		this.ip = ip;
		this.source = source;
	}
}
