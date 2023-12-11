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

/**
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "商家分销商品")
public class ShopDistributionQuery extends PageParams {

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "商品类目")
	private Long categoryId;

	@Schema(description = "店铺类目")
	private Long shopCategoryId;

	@Schema(description = "商品名字")
	private String name;

	@Schema(description = "是否参与分销 0:否, 1:是")
	private Integer distributionFlag;

	@Schema(description = "商品状态(不需要船)")
	private Integer status;

	@Schema(description = "审核状态(不需要传)")
	private Integer opStatus;

	@Schema(description = "商品id")
	private String productIdList;

}
