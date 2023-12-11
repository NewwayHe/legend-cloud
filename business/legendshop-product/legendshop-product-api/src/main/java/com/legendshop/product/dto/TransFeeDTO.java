/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import cn.hutool.core.util.NumberUtil;
import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 运输费用(TransFee)DTO
 *
 * @author legendshop
 * @since 2020-09-04 17:14:16
 */
@Data
@Schema(description = "运输费用(TransFee)DTO")
public class TransFeeDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -63296974056984138L;

	/**
	 * 模板ID
	 */
	@Schema(description = "模板ID")
	private Long transId;

	/**
	 * 续件运费
	 */
	@NotNull(message = "续件运费不能为空")
	@DecimalMin(value = "0", message = "续件运费必须大于等于0")
	@Schema(description = "续件运费")
	private BigDecimal addPrice;

	/**
	 * 首件运费
	 */
	@NotNull(message = "首件运费不能为空")
	@Min(value = 0, message = "首件运费必须大于等于0")
	@DecimalMin(value = "0", message = "首件运费必须大于等于0")
	@Schema(description = "首件运费")
	private BigDecimal firstPrice;

	/**
	 * 续件
	 */
	@NotNull(message = "续件数量不能为空")
	@Schema(description = "续件")
	@DecimalMin(value = "0", message = "必须要正数")
	private Double addNum;

	/**
	 * 首件
	 */
	@NotNull(message = "首件数量不能为空")
	@DecimalMin(value = "0", message = "必须要正数")
	@Schema(description = "首件")
	private Double firstNum;

	/**
	 * {@link com.legendshop.product.enums.TransTypeEnum}
	 */
	@Schema(description = "运费模板计费方式 1、按件数 2、按重量 3、按体积 4、固定运费")
	private String calFreightType;

	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Integer status;

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

	@Schema(description = "地区名称")
	private String area;

	@AssertTrue(message = "首件数量(体积/重量)必须大于0")
	public Boolean getCheckFirstNum() {
		if (null != firstNum && NumberUtil.compare(firstNum, 0d) > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@AssertTrue(message = "续件数量(体积/重量)必须大于0")
	public Boolean getCheckAddNum() {
		if (null != addNum && NumberUtil.compare(addNum, 0d) > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
