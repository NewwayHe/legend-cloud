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
 * 类目关联管理-参数组关联DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理-参数组关联DTO")
public class ProductPropertyAggParamGroupDTO implements Serializable {

	private static final long serialVersionUID = -5497389501607234488L;
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
	 * 参数组ID
	 */
	@Schema(description = "参数组ID")
	private Long groupId;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;

}
