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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商城钱包设置(WalletSetting)DTO
 *
 * @author legendshop
 * @since 2021-03-24 17:22:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商城钱包设置DTO")
public class WalletSettingDTO implements Serializable {

	/**
	 * 是否启用商城钱包系统
	 */
	@Schema(description = "是否启用商城钱包系统")
	private Boolean enabled;

	/**
	 * 是否开启提现
	 */
	@Schema(description = "是否开启提现")
	private Boolean withdraw;

	/**
	 * 是否需要平台审核
	 */
	@Schema(description = "是否需要平台审核")
	private Boolean withdrawAudit;
}
