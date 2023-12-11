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

/**
 * 类目关联管理跟品牌的关系表(ProductPropertyAggBrand)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理-品牌关联DTO")
public class ProductPropertyAggBrandDTO implements Serializable {


	private static final long serialVersionUID = -5071651894568363113L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 聚合Id
	 */
	@Schema(description = "聚合Id")
	private Long aggId;


	/**
	 * 品牌Id
	 */
	@Schema(description = "品牌Id")
	private Long brandId;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;

}
