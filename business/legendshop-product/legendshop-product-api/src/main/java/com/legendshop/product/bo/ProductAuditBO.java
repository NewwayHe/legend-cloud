/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品审核BO
 *
 * @author legendshop
 * @create: 2021-08-17 11:50
 */
@Data
public class ProductAuditBO extends ProductBO {

	private static final long serialVersionUID = 5331721640243208108L;

	@Schema(description = "审核内容")
	private String auditContent;
}
