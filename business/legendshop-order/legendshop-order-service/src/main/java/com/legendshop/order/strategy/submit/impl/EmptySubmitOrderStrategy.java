/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.submit.impl;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.strategy.submit.SubmitOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 提交订单的空策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class EmptySubmitOrderStrategy implements SubmitOrderStrategy {


	@Override
	public R submit(ConfirmOrderBO confirmOrderBo) {

		log.info("进入确认订单策略的空策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		return null;
	}
}
