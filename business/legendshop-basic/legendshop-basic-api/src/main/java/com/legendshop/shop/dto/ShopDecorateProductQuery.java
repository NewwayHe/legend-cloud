/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商家店铺装修商品列表查询
 *
 * @author legendshop
 * @create: 2021-01-11 17:28
 */
@Data
@Schema(description = "商家店铺装修商品列表查询")
public class ShopDecorateProductQuery extends PageParams {

	private static final long serialVersionUID = -8583456126622184080L;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "分类ID")
	private Long categoryId;

	@Schema(description = "商家ID")
	private Long shopId;

	@Schema(description = "排序")
	private String order;
}
