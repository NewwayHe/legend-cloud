/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存历史表(StockLog)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "库存历史表DTO")
public class StockLogDTO implements Serializable {


	private static final long serialVersionUID = 1362301803271465851L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;


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


	/**
	 * 变更之前的库存
	 */
	@Schema(description = "变更之前的库存")
	private Integer beforeStock;


	/**
	 * 变更之后的库存
	 */
	@Schema(description = "变更之后的库存")
	private Integer afterStock;


	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;


	/**
	 * 更新备注
	 */
	@Schema(description = "更新备注")
	private String updateRemark;

}
