/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import com.legendshop.common.core.validator.group.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户反馈批量处理
 *
 * @author legendshop
 */
@Schema(description = "用户反馈批量处理DTO")
@Data
public class UserFeedbackBatchHandleDTO implements Serializable {


	private static final long serialVersionUID = 2355335664751076747L;


	@Schema(description = "处理人ID")
	private Long respUserId;

	/**
	 * 处理意见
	 */
	@Schema(description = "处理意见")
	@NotBlank(message = "处理意见不能为空", groups = Update.class)
	private String respContent;


	/**
	 * id 集合
	 */
	@Schema(description = "id 集合")
	private List<Long> ids;
}
