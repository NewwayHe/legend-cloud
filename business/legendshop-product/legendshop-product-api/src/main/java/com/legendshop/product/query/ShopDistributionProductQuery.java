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
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "商家分销商品")
public class ShopDistributionProductQuery extends PageParams {
	/**
	 * 用户ID
	 */

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "商品类目")
	private List<Long> productIdList;

}
