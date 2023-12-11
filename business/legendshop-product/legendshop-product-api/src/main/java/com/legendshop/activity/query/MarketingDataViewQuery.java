/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author legendshop
 */
@Schema(description = "商品访问概况查询条件")
@Data
public class MarketingDataViewQuery {

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;

	/**
	 * 店铺Id
	 */
	@Schema(description = "店铺Id")
	private Long shopId;

	/**
	 * 升降序
	 */
	@Schema(description = "升降序:true 升序 false 降序")
	private Boolean order;

	/**
	 * 排序方式
	 */
	@Schema(description = "排序方式")
	private String prop;
}
