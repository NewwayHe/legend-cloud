/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 条件包邮(TransFree)DTO
 *
 * @author legendshop
 * @since 2020-09-04 17:14:19
 */
@Data
@Schema(description = "条件包邮(TransFree)DTO")
public class TransFreeDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 967488072456674585L;

	/**
	 * 模板id
	 */
	@Schema(description = "模板id")
	private Long transId;

	/**
	 * 满件包邮
	 */
	@Schema(description = "满件包邮")
	private Integer num;

	/**
	 * 满多少金额包邮
	 */
	@Schema(description = "满多少金额包邮")
	private BigDecimal price;

	@NotNull(message = "数值不能为空")
	@DecimalMin(value = "0", message = "满包数值必须大于等于0")
	@Schema(description = "数值")
	private BigDecimal number;

	/**
	 * {@link com.legendshop.product.enums.TransFreeTypeEnum}
	 */
	@Schema(description = "条件包邮类型 1、满件包邮 2、满金额包邮")
	private Integer type;

	@Schema(description = "记录时间")
	private Date recDate;

	/**
	 * 选择可支持销售的地区
	 */
	@Valid
	@Schema(description = "选择可支持销售的地区")
	private List<TransCityDTO> transCityDTOList;

	@Valid
	@NotEmpty(message = "省份不能为空")
	@Schema(description = "选中的省份")
	private List<TransProvinceDTO> provinceList;

	/**
	 * 配送至
	 */
	@Schema(description = "配送至")
	private String area;

}
