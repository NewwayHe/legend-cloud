/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class ProductCommentAuditDTO implements Serializable {

	private List<Long> ids;

	private List<Long> addIds;

	@NotNull(message = "审核状态不能为空")
	private Integer status;
}
