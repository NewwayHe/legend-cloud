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
 * @author legendshop
 */
@Schema(description = "商品搜索概况查询条件")
@Data
public class ProductDataSearchPicQuery extends PageParams {

	/**
	 * 关键词
	 */
	@Schema(description = "关键词")
	private String word;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private String startDate;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private String endDate;

	/**
	 * 排序方式
	 */
	@Schema(description = "排序方式")
	private String prop;

	/**
	 * 升降序
	 */
	@Schema(description = "升降序")
	private String order;

}
