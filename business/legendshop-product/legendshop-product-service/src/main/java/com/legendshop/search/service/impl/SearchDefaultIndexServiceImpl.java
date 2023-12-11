/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.service.IndexService;
import com.legendshop.search.service.SearchCouponIndexService;
import com.legendshop.search.service.SearchProductIndexService;
import com.legendshop.search.service.SearchShopIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title SearchDefaultIndexServiceImpl
 * @date 2022/3/23 10:27
 * @description： 默认的
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchDefaultIndexServiceImpl implements IndexService {

	private final SearchShopIndexService shopIndexService;
	private final SearchCouponIndexService couponIndexService;
	private final SearchProductIndexService productIndexService;

	@Override
	public boolean isSupport(String indexType) {
		return IndexTypeEnum.ALL_INDEX.name().equals(indexType);
	}

	@Override
	public Class<?> getSupportClass() {
		return null;
	}

	@Override
	public String initIndex(Integer targetType, List<Long> targetId) throws IOException {

		couponIndexService.initAllCouponIndex();
		shopIndexService.initAllShopIndex();
		productIndexService.initAllProductIndex();

		return CommonConstants.OK;
	}

	@Override
	public String delIndex(Integer targetType, List<Long> targetId) {
		return CommonConstants.OK;
	}

	@Override
	public String updateIndex(Integer targetType, List<Long> targetId) {
		return CommonConstants.OK;
	}
}
