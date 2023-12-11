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
 * @author legendshop
 */
@Data
@Schema(description = "结算账单查询")
public class ShopOrderBillQuery extends PageParams {

	private static final long serialVersionUID = 4643386337481881338L;


	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String shopName;

	/**
	 * 档期
	 */
	@Schema(description = "档期")
	private String flag;

	/**
	 * 店铺ID
	 */
	@Schema(hidden = true)
	private Long shopId;

	/**
	 * 订单结算编号
	 */
	@Schema(description = "订单结算编号")
	private String Sn;


	/**
	 * 1默认 2店家已确认  3平台已审核  4结算完成
	 */
	@Schema(description = "1默认 2店家已确认  3平台已审核  4结算完成")
	private String status;

	@Schema(description = "档期时间")
	private String startTime;
}
