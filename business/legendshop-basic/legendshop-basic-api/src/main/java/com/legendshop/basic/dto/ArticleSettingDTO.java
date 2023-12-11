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
 * @author legendshop
 */
@Data
public class ArticleSettingDTO implements Serializable {

	private static final long serialVersionUID = -6768826365308943224L;

	@Schema(description = "是否开启广告图")
	private Boolean adsEnabled;

	@Schema(description = "是否开启自动审核文章")
	private Boolean verifyEnabled;

}
