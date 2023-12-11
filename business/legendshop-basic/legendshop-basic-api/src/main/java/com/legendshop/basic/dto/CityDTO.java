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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 城市
 *
 * @author legendshop
 */
@Data
@Schema(description = "城市")
public class CityDTO implements Serializable {

	private static final long serialVersionUID = -7087216615560835670L;

	@NotNull(message = "地区ID不能为空")
	@Schema(description = "地区ID", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Schema(description = "地区编码")
	private String code;

	@Schema(description = "地区名")
	private String name;

}
