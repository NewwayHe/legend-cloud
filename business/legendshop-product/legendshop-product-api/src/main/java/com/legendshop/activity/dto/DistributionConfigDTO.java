/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分销配置DTO
 *
 * @author legendshop
 * @create: 2021-11-02 15:24
 */
@Data
public class DistributionConfigDTO implements Serializable {

	private static final long serialVersionUID = 5762287771435352485L;

	/**
	 * 分销规则开关（总开关）  {@link RulesSwitchEnum}
	 */
	@Schema(description = "分销规则开关（总开关）-1表示未设置基础设置  -2表示未设置结算设置 0表示已经全部设置完可开启分销  1开启分销 ")
	private Integer rulesSwitch;

	@Schema(description = "商品分享语")
	private String sharePosterText;
}
