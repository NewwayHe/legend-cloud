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
@Schema(description = "商品分组关联参数")
public class ProductGroupRelationQuery extends PageParams {

	@NotNull(message = "分组id不能为空")
	@Schema(description = "商品分组ID")
	private Long groupId;

	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品名")
	private String productName;

	@Schema(description = "店铺名")
	private String shopName;

	@Schema(description = "排序方式，支持")
	private String sort;

	/**
	 * 升序降序
	 */
	@Schema(description = "升序降序，true为降序， false为升序，不传默认为降序")
	private Boolean descending;
}
