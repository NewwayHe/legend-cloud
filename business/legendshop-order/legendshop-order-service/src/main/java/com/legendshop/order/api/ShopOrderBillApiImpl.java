/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.service.ShopOrderBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class ShopOrderBillApiImpl implements ShopOrderBillApi {

	final ShopOrderBillService shopOrderBillService;

	@Override
	public R<Void> calculateBillJobHandle(Date startDate, Date endDate) {
		return shopOrderBillService.calculateBillJobHandle(startDate, endDate);
	}
}
