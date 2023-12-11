/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm.impi;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.ConfirmOrderDTO;
import com.legendshop.order.strategy.confirm.ConfirmOrderStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 确认订单的空策略
 *
 * @author legendshop
 */
@Slf4j
@Service
public class EmptyConfirmOrderStrategy implements ConfirmOrderStrategy {


	@Override
	public R<ConfirmOrderBO> confirm(ConfirmOrderBO confirmOrderBo) {
		log.info("进入确认订单策略的空策略, params: {}", JSONUtil.toJsonStr(confirmOrderBo));
		return null;
	}

	@Override
	public R<ConfirmOrderBO> check(ConfirmOrderDTO confirmOrderDTO) {
		log.info("进入下单检查以及组装商品信息的空策略, params: {}", JSONUtil.toJsonStr(confirmOrderDTO));
		return null;
	}


	@Override
	public R<ConfirmOrderBO> handleSpecificBusiness(ConfirmOrderBO confirmOrderBO) {
		log.info("进入处理特殊业务的空策略, params: {}", JSONUtil.toJsonStr(confirmOrderBO));
		return null;
	}
}
