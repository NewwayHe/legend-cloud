/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.refund.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ConfirmRefundDTO;
import com.legendshop.order.entity.OrderRefundReturn;
import com.legendshop.order.strategy.refund.ConfirmRefundStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 平台确认退款的空策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class EmptyConfirmRefundStrategy implements ConfirmRefundStrategy {
	@Override
	public R confirmRefund(ConfirmRefundDTO confirmRefundDTO) {
		log.info("进入平台确认退款策略的空策略, params: {}", JSONUtil.toJsonStr(confirmRefundDTO));
		return null;
	}

	@Override
	public R<Void> refundHandler(OrderRefundReturn orderRefundReturn) {
		log.info("进入平台退款处理策略的空策略, params: {}", JSONUtil.toJsonStr(orderRefundReturn));
		return null;
	}

	@Override
	public R<Void> refundCallback(String refund) {
		return null;
	}
}
