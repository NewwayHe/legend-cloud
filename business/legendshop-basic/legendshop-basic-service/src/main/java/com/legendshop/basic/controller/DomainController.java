/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.dto.CommonDomainDTO;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/domain", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class DomainController {

	private final CommonProperties commonProperties;

	@GetMapping
	public R<CommonDomainDTO> getDomain() {
		CommonDomainDTO commonDomainDTO = CommonDomainDTO.builder()
				.userPcDomainName(commonProperties.getUserPcDomainName())
				.userMobileDomainName(commonProperties.getUserMobileDomainName()).build();
		return R.ok(commonDomainDTO);
	}

}
