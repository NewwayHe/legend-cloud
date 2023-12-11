/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 退款退货搜索对象
 *
 * @author legendshop
 */
@Data
@Schema(description = "退款退货搜索参数")
public class OrderBulkRefundQuery {

	@Schema(description = "退款编号")
	private List<Long> refundIds;

	@Schema(description = "是否通过售后")
	private Boolean auditFlag;

	@Schema(description = "卖家备注")
	private String sellerMessage;

	@Schema(description = "是否弃货")
	private Boolean abandonedGoodFlag;
}
