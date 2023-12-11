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

/**
 * 库存历史表(StockLog)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "库存历史参数查询")
public class StockLogQuery extends PageParams {

	private static final long serialVersionUID = 5803807251972676507L;

	/**
	 * 商品Id
	 */
	@Schema(description = "商品Id")
	private Long productId;


	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String name;

}
