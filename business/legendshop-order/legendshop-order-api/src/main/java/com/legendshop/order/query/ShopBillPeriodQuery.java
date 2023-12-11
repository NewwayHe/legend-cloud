/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 结算档期
 *
 * @author legendshop
 */
@Data
@Schema(description = "结算档期查询参数")
public class ShopBillPeriodQuery extends PageParams {


	/**
	 * 结算档期
	 */
	@Schema(description = "结算档期")
	private String flag;

	@Schema(description = "档期时间")
	private String startTime;
}
