/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.basic.dto.CityDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 运费省份
 *
 * @author legendshop
 */
@Data
@Schema(description = "运费省份")
public class TransProvinceDTO implements Serializable {

	private static final long serialVersionUID = -649738960773778318L;

	@NotNull(message = "省份id不能为空")
	@Schema(description = "省份ID")
	private Long id;

	@Schema(description = "地区名")
	private String name;

	@NotNull(message = "状态不能为空")
	@Schema(description = "选中状态")
	private Boolean selectFlag;

	@Valid
	@NotEmpty(message = "城市不能为空")
	@Schema(description = "城市")
	private List<CityDTO> children;
}
