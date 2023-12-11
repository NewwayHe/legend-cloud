/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 百度统计查询
 *
 * @author legendshop
 * @create: 2021-06-19 17:35
 */
@Data
public class BaiduViewQuery extends PageParams {

	private static final long serialVersionUID = -3124024720213321673L;

	@Schema(description = "开始时间")
	private String startDate;

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
