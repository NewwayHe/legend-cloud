/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.pay.dao.ShopIncomingDao;
import com.legendshop.pay.entity.ShopIncoming;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.legendshop.common.core.constant.CacheConstants.SHOP_INCOMING_MERCHANT_NO;

/**
 * 商家进件表(ShopIncoming)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-12 09:27:59
 */
@Repository
public class ShopIncomingDaoImpl extends GenericDaoImpl<ShopIncoming, Long> implements ShopIncomingDao {

	@Override
	public ShopIncoming getByShopId(Long shopId) {
		if (shopId == null) {
			return null;
		}
		LambdaEntityCriterion<ShopIncoming> criterion = new LambdaEntityCriterion<>(ShopIncoming.class);
		criterion.eq(ShopIncoming::getShopId, shopId);
		return getByProperties(criterion);
	}

	@Override
	public ShopIncoming getByRequestNo(String requestNo) {
		LambdaEntityCriterion<ShopIncoming> criterion = new LambdaEntityCriterion<>(ShopIncoming.class);
		criterion.eq(ShopIncoming::getRequestNo, requestNo);
		return getByProperties(criterion);
	}

	@Override
	public List<ShopIncoming> getByShopId(List<Long> shopIds) {
		if (CollUtil.isEmpty(shopIds)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<ShopIncoming> criterion = new LambdaEntityCriterion<>(ShopIncoming.class);
		criterion.in(ShopIncoming::getShopId, shopIds);
		return queryByProperties(criterion);
	}

	@Override
	@Cacheable(cacheNames = SHOP_INCOMING_MERCHANT_NO, key = "#shopId", unless = "#result == null")
	public String getMerchantNoByShopId(Long shopId) {
		if (null == shopId) {
			return null;
		}

		ShopIncoming shopIncoming = getByShopId(shopId);
		return shopIncoming.getMerchantNo();
	}

}
