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
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.UserInvoiceForOrderDTO;
import com.legendshop.user.service.UserInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class UserInvoiceApiImpl implements UserInvoiceApi {

	private final UserInvoiceService userInvoiceService;

	@Override
	public R<UserInvoiceBO> getInvoiceBoById(Long invoiceId) {
		return R.ok(this.userInvoiceService.getInvoiceBoById(invoiceId));
	}

	@Override
	public R<UserInvoiceBO> getUserInvoiceForOrder(UserInvoiceForOrderDTO invoiceForOrderDTO) {
		return R.ok(this.userInvoiceService.getUserInvoiceForOrder(invoiceForOrderDTO.getUserId(), invoiceForOrderDTO.getInvoiceTypeList()));
	}
}
