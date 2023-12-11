/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.FreightModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 配送方式计算DTO
 *
 * @author legendshop
 */
@Schema(description = "配送方式")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransFeeCalcDTO extends BaseDTO implements Serializable {

	/**
	 * 运费.
	 */
	@Schema(description = "运费")
	private BigDecimal deliveryAmount;

	/**
	 * 运费模板.
	 */
	@Schema(description = "运费模板")
	private String freightMode;

	/**
	 * 描述.
	 */
	@Schema(description = "描述")
	private String desc;


	public TransFeeCalcDTO(BigDecimal deliveryAmount, String freightMode) {
		super();
		this.deliveryAmount = deliveryAmount;
		this.freightMode = freightMode;
	}

	/**
	 * Gets the desc.
	 *
	 * @return the desc
	 */
	public String getDesc() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		StringBuilder sb = new StringBuilder();
		if (StrUtil.isNotBlank(freightMode)) {
			if (FreightModeEnum.TRANSPORT_MAIL.value().equals(freightMode)) {
				sb.append("平邮[").append(nf.format(deliveryAmount)).append("元]");
			} else if (FreightModeEnum.TRANSPORT_EXPRESS.value().equals(freightMode)) {
				sb.append("快递[").append(nf.format(deliveryAmount)).append("元]");
			} else if (FreightModeEnum.TRANSPORT_EMS.value().equals(freightMode)) {
				sb.append("EMS[").append(nf.format(deliveryAmount)).append("元]");
			}
		} else {
			sb.append("免邮");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((freightMode == null) ? 0 : freightMode.hashCode());
		return result;
	}

	/** (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TransFeeCalcDTO other = (TransFeeCalcDTO) obj;
		if (freightMode == null) {
			if (other.freightMode != null) {
				return false;
			}
		} else if (!freightMode.equals(other.freightMode)) {
			return false;
		}
		return true;
	}
}
