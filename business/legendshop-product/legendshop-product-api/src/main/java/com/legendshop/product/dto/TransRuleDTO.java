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
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.TransRuleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺运费规则(TransRule)DTO
 *
 * @author legendshop
 * @since 2020-09-08 17:00:53
 */
@Data
@Schema(description = "店铺运费规则(TransRule)DTO")
public class TransRuleDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -24439168831679932L;

	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * {@link com.legendshop.product.enums.TransRuleTypeEnum}
	 */
	@EnumValid(target = TransRuleTypeEnum.class)
	@Schema(description = "店铺运费规则 1：叠加运算 2：按最高值计算 ")
	private Integer type;

	@Schema(description = "更新时间")
	private Date recDate;

}
