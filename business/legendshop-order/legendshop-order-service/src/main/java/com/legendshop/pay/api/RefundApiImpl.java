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
import com.legendshop.pay.dto.RefundDTO;
import com.legendshop.pay.dto.RefundResultDTO;
import com.legendshop.pay.service.RefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RefundApiImpl implements RefundApi {

	final RefundService refundService;

	@Override
	public R<RefundResultDTO> refund(@RequestBody RefundDTO refundDTO) {
		return this.refundService.refund(refundDTO);
	}
}
