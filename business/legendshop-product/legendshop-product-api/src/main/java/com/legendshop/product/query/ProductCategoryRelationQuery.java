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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品类目关联参数")
public class ProductCategoryRelationQuery extends PageParams {

	private static final long serialVersionUID = 3631383252317561435L;

	@NotNull(message = "类目id不能为空")
	@Schema(description = "商品类目ID")
	private Long categoryId;

	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "店铺ID")
	private Long shopId;

	@Schema(description = "商品名")
	private String productName;

	@Schema(description = "店铺名")
	private String shopName;

	@Schema(description = "排序方式 createTime: 创建时间 updateTime: 跟新时间 ")
	private String sort;

}
