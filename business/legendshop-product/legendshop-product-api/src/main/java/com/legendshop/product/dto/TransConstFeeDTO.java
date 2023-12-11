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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 固定运费(TransConstFee)DTO
 *
 * @author legendshop
 * @since 2020-09-07 14:43:49
 */
@Data
@Schema(description = "固定运费(TransConstFee)DTO")
public class TransConstFeeDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -69696740603051247L;

	/**
	 * 模板id
	 */
	@Schema(description = "模板id")
	private Long transId;

	/**
	 * 固定运费
	 */
	@Schema(description = "固定运费")
	@NotNull(message = "固定运费不能为空")
	private BigDecimal constantPrice;

	@Schema(description = "记录时间")
	private Date recDate;

	/**
	 * 选择可支持销售的地区
	 */
	@Schema(description = "选择可支持销售的地区")
	private List<TransCityDTO> transCityDTOList;

	@NotEmpty(message = "省份不能为空")
	@Schema(description = "选中的省份")
	private List<TransProvinceDTO> provinceList;

	@Schema(description = "地区名称")
	private String area;
}
