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
 * 参数组表(ParamGroup)搜索DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "参数组查询参数")
@Accessors(chain = true)
public class ParamGroupQuery extends PageParams {


	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String name;


	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer seq;

	/**
	 * 规格来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "规格来源:USER商家自定义 SYS平台")
	private String source;

}
