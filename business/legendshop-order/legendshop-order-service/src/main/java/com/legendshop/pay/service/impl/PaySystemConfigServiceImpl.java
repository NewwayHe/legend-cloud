/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.pay.dto.PayConfigDTO;
import com.legendshop.pay.service.PaySystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySystemConfigServiceImpl implements PaySystemConfigService {

	final SysParamsApi sysParamsApi;

	@Override
	public PayConfigDTO getConfig(String payCode) {
		PayConfigDTO dto = new PayConfigDTO();
		dto.setName(payCode);
		List<SysParamItemDTO> itemList = this.sysParamsApi.getSysParamItemsByParamName(payCode).getData();
		if (CollectionUtils.isEmpty(itemList)) {
			return dto;
		}
		dto.setItemList(itemList);
		return dto;
	}
}
