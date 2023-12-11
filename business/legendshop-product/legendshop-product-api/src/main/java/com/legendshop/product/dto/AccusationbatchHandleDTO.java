/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.product.enums.AccusationIllegalOffEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量处理商品举报类
 *
 * @author legendshop
 */
@Data
@Schema(description = "批量处理商品举报DTO")
public class AccusationbatchHandleDTO implements Serializable {

	private static final long serialVersionUID = -8640228952266509071L;

	/**
	 * 处理结果  -1:无效举报 1：有效举报  -2：恶意举报
	 */
	@NotNull(message = "处理结果不能为空")
	@Schema(description = "处理结果: -1:无效举报 1：有效举报  -2：恶意举报")
	private Integer result;

	/**
	 * 处理操作 0：不处理 1：违规下架
	 */
	@EnumValid(target = AccusationIllegalOffEnum.class, message = "处理状态不匹配")
	@Schema(description = "处理操作: 0：不处理 1：违规下架")
	private Integer illegalOff;

	/**
	 * 处理意见
	 */
	@Schema(description = "处理意见")
	private String handleInfo;

	/**
	 * 处理意见
	 */
	@Schema(description = "处理人")
	private String handler;

	/**
	 * 举报id列表
	 */
	@NotEmpty(message = "举报id列表不能为空")
	@Schema(description = "举报id列表")
	private List<Long> accusationIds;
}
