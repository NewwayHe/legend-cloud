/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.data.dao.ShopOutStockRateDao;
import com.legendshop.data.entity.ShopOutStockRate;
import com.legendshop.data.service.ShopOutStockRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 店铺缺货率
 *
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class ShopOutStockRateServiceImpl implements ShopOutStockRateService {

	final ShopOutStockRateDao shopOutStockRateDao;

	@Override
	public R<Void> shopOutStockRateJobHandle() {

		List<ShopOutStockRate> shopOutStockRates = shopOutStockRateDao.queryShopOutStockRate();
		if (CollUtil.isEmpty(shopOutStockRates)) {
			return R.ok();
		}
		BigDecimal hundred = new BigDecimal(100);
		for (ShopOutStockRate shopOutStockRate : shopOutStockRates) {
			shopOutStockRate.setCreateTime(DateUtil.date());
			shopOutStockRate.setOutStockRate(Optional.ofNullable(shopOutStockRate.getOutStockRate()).orElse(BigDecimal.ZERO).multiply(hundred));
		}
		shopOutStockRateDao.save(shopOutStockRates);
		return R.ok();
	}
}
