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
import com.legendshop.user.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class PassportApiImpl implements PassportApi {

	final PassportService passportService;

	@Override
	public R<String> getOpenIdByUserId(Long userId, String source) {
		return this.passportService.getOpenIdByUserId(userId, source);
	}

	@Override
	public R<List<String>> getOpenIdByUserIds(List<Long> userIds, String source) {
		return passportService.getOpenIdByUserIds(userIds, source);
	}

	@Override
	public R<Boolean> unboundUserPassport(Long userId, String type, String source) {
		return passportService.unboundUserPassport(userId, type, source);
	}
}
