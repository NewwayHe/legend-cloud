/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dao.OrderItemDao;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.service.DistributionUtil;
import com.legendshop.pay.api.UserWalletApi;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 分销处理实现
 *
 * @author legendshop
 */
@Slf4j
@Service
@AllArgsConstructor
public class DistributionUtilImpl implements DistributionUtil {

	final OrderItemDao orderItemDao;
	final UserWalletApi userWalletApi;
	final StringRedisTemplate redisTemplate;


	/**
	 * 佣金计算
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	@Override
	public R<ConfirmOrderBO> buildCommissionInfo(ConfirmOrderBO confirmOrderBO) {
		log.info("###### 进入佣金计算 ######");
		log.info("###### 平台关闭分销，佣金处理完成 ######");
		return R.ok(confirmOrderBO);

	}

	@Override
	public R<ConfirmOrderBO> calculationCommission(ConfirmOrderBO confirmOrderBO) {
		BigDecimal hundred = BigDecimal.valueOf(100);
		confirmOrderBO.getShopOrderList().stream().map(SubmitOrderShopDTO::getSkuList).flatMap(List::stream).filter(SubmitOrderSkuDTO::getDistFlag).forEach(skuDTO -> {
			// 总佣金 = 实付金额（不包含积分抵扣） * 商品佣金比例
			BigDecimal totalCommissionCash = skuDTO.getTotalActualAmount().multiply(skuDTO.getDistRatio().divide(hundred, 6, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
			skuDTO.setDistCommissionCash(totalCommissionCash);
			skuDTO.setSelfPurchaseTotalAmount(totalCommissionCash.multiply(skuDTO.getFirstCommissionRate().divide(hundred, 6, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN));
			skuDTO.setSelfPurchaseAmount(skuDTO.getSelfPurchaseTotalAmount().divide(new BigDecimal(skuDTO.getTotalCount()), 2, RoundingMode.DOWN));
		});
		return R.ok(confirmOrderBO);
	}

	@Override
	public R checkCommission(ConfirmOrderBO confirmOrderBO) {
		log.info("###### 进入佣金校验 ######");
		log.info("###### 平台关闭分销，佣金处理完成 ######");
		return R.ok();
	}


}
