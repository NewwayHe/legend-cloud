/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分销用户DTO
 *
 * @author legendshop
 * @create: 2021-03-24 16:26
 */
@Data
public class DistributionOrdinaryUserDTO extends OrdinaryUserDTO {

	private static final long serialVersionUID = -615508279678409399L;

	@Schema(description = "分销层级")
	private Integer grade;
}
