/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城钱包设置(WalletSetting)BO
 *
 * @author legendshop
 * @since 2021-03-24 17:22:12
 */
@Data
public class WalletSettingBO implements Serializable {

	private static final long serialVersionUID = -70579162465178207L;

	private Long id;

	/**
	 * 是否启用商城钱包系统
	 */
	@Schema(description = "是否启用商城钱包系统")
	private Integer enable;

	/**
	 * 是否开启提现
	 */
	@Schema(description = "是否开启提现")
	private Integer withdraw;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private Date updateTime;

}
