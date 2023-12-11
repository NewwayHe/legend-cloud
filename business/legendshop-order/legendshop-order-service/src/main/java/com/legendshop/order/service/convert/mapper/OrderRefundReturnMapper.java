/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert.mapper;

import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.enums.OrderRefundReturnStatusEnum;

/**
 * @author legendshop
 * @create: 2020-12-24 10:10
 */
public class OrderRefundReturnMapper {

	public static Integer convert2Status(OrderItem item) {
		if (OrderRefundReturnStatusEnum.ITEM_UNDO_APPLY.value().equals(item.getRefundStatus()) || OrderRefundReturnStatusEnum.ITEM_NO_REFUND.value().equals(item.getRefundStatus())) {
			return 1;
		}
		return -1;
	}
}
