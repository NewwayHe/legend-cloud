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
 * 活动设置DTO
 *
 * @author legendshop
 * @create: 2021-11-02 15:23
 */
@Data
public class ActivityConfigDTO implements Serializable {

	private static final long serialVersionUID = 1355770046954113078L;


	@Schema(description = "分销配置")
	private DistributionConfigDTO distributionConfig;
}
