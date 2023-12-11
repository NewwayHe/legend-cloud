/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.legendshop.product.enums.ProductDelStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品类目-商品关系表(ProdGroupRelevance)BO
 *
 * @author legendshop
 */
@Schema(description = "商品类目关联表查询参数")
@Data
public class ProductCategoryRelationBO implements Serializable {

	private static final long serialVersionUID = -23757147440925291L;
	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;


	/**
	 * 商品类目ID
	 */
	@Schema(description = "商品类目ID")
	private Long categoryId;


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
	 * 品牌名称
	 **/
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 销量
	 */
	@Schema(description = "已经销售数量")
	private Integer buys;


	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态: -2：永久删除；-1：删除；0：下线；1：上线；2：违规；3：全部")
	private Integer status;

	/**
	 * 审核操作状态 {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;


}
