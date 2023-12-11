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

import java.io.Serializable;

/**
 * 配置
 *
 * @author legendshop
 * @create: 2021-11-03 18:16
 */
@Data
public class SettingDTO implements Serializable {

	private static final long serialVersionUID = 763232187090624716L;

	@Schema(description = "系统配置")
	private SystemConfigDTO systemConfig;

	@Schema(description = "主题颜色")
	private String themesColor;


	@Schema(description = "PC主题颜色")
	private String pcThemesColor;

	@Schema(description = "系统时间")
	private Long serverNowDate;

	@Schema(description = "相关地址")
	private CommonPropertiesDTO urlProperties;

	@Schema(description = "装修页面")
	private String tabbar;
}
