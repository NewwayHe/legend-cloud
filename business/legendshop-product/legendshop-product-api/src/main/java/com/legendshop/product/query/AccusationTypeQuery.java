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
@Data
@Schema(description = "商品举报类型查询参数")
public class AccusationTypeQuery extends PageParams {
	/**
	 * 名称
	 */
	@Schema(description = "举报名称")
	private String name;


	/**
	 * 状态
	 */
	@Schema(description = "状态")
	private Integer status;
}
