/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.UserInvoiceForOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author legendshop
 */
@FeignClient(contextId = "userInvoiceApi", value = ServiceNameConstants.USER_SERVICE)
public interface UserInvoiceApi {

	String PREFIX = ServiceNameConstants.USER_SERVICE_RPC_PREFIX;

	/**
	 * 获取发票信息
	 */
	@GetMapping(value = PREFIX + "/p/user/invoice/getInvoiceBoById")
	R<UserInvoiceBO> getInvoiceBoById(@RequestParam(value = "invoiceId") Long invoiceId);

	@PostMapping(value = PREFIX + "/p/user/invoice/getUserInvoiceForOrder")
	R<UserInvoiceBO> getUserInvoiceForOrder(@RequestBody UserInvoiceForOrderDTO invoiceForOrderDTO);
}
