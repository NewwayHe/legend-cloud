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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "修改配置项dto")
public class SysParamValueItemDTO implements Serializable {

	@NotNull(message = "id不能为空")
	@Schema(description = "id")
	private Long id;

	@NotBlank(message = "value值不能为空")
	@Schema(description = "值")
	private String value;
}
