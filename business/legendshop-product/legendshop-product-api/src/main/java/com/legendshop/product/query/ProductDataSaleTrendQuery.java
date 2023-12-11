/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品销售趋势图查询参数")
public class ProductDataSaleTrendQuery extends PageParams {

	@Schema(description = "商品id")
	private Long productId;

	@Schema(description = "skuId")
	private Long skuId;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "开始时间")
	private String startDate;

	@Schema(description = "结束时间")
	private String endDate;

	@Schema(description = "排序方式")
	private String prop;

	@Schema(description = "升降序")
	private String order;

	private Date sDate;

	private Date eDate;
}
