/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.pay.dto.PayRefundSettlementDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "payRefundSettlementApi", value = ServiceNameConstants.ORDER_SERVICE)
public interface PayRefundSettlementApi {

	String PREFIX = ServiceNameConstants.ORDER_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/payRefundSettlement/getByPayRefundSn")
	R<PayRefundSettlementDTO> getByPayRefundSn(@RequestParam("payRefundSn") String payRefundSn);

	@GetMapping(value = PREFIX + "/payRefundSettlement/getByRefundSn")
	R<List<PayRefundSettlementDTO>> queryByRefundSn(@RequestParam("refundSn") String refundSn);
}
