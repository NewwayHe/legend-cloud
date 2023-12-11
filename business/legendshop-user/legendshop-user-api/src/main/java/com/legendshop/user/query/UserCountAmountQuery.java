/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "用户数量统计查询")
public class UserCountAmountQuery extends PageParams {

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
	@Schema(description = "结束时间")
	private String prop;

	/**
	 * 升降序
	 */
	@Schema(description = "升降序")
	private String order;

}
