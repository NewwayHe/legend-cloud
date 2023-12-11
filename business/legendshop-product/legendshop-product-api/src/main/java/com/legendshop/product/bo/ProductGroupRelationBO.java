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

import java.io.Serializable;

/**
 * 商品分组-商品关系表(ProdGroupRelevance)BO
 *
 * @author legendshop
 */
@Schema(description = "商品分组关联表查询参数")
@Data
public class ProductGroupRelationBO implements Serializable {


	private static final long serialVersionUID = -4686788700949054727L;
	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;


	/**
	 * 商品分组ID
	 */
	@Schema(description = "商品分组ID")
	private Long groupId;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品名")
	private String productName;

	@Schema(description = "商品销售价范围")
	private String price;

	@Schema(description = "店铺名")
	private String shopName;

	@Schema(description = "图片")
	private String pic;

	/**
	 * {@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态")
	private Integer status;

}
