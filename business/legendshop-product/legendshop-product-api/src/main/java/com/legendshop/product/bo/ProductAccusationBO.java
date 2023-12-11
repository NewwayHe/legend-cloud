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

import java.util.Date;

/**
 * 商品举报信息
 *
 * @author legendshop
 * @create: 2021-02-23 14:22
 */
@Data
public class ProductAccusationBO extends ProductBO {

	private static final long serialVersionUID = 5200396451630950658L;

	@Schema(description = "举报时间")
	private Date accusationTime;

	@Schema(description = "举报内容")
	private String accusationContent;

	@Schema(description = "商品举报类型名称")
	private String accusationTypeName;
}
