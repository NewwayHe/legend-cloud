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
import com.legendshop.user.bo.UserContactBO;
import com.legendshop.user.service.UserContactService;
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
public class UserContactApiImpl implements UserContactApi {

	private final UserContactService userContactService;

	@Override
	public R<UserContactBO> getDefaultUserContact(Long userId) {
		return R.ok(this.userContactService.getDefaultUserContact(userId));
	}

	@Override
	public R<UserContactBO> getById(Long id) {
		return R.ok(this.userContactService.getBoById(id));
	}

	@Override
	public R<UserContactBO> getUserContactForOrder(Long userId, Long contactId) {
		return R.ok(this.userContactService.getUserContactForOrder(userId, contactId));
	}
}
