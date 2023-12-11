/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.query.OrdinaryUserQuery;
import com.legendshop.user.service.OrdinaryUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class OrdinaryUserApiImpl implements OrdinaryUserApi {

	private final OrdinaryUserService ordinaryUserService;

	@Override
	public R<List<OrdinaryUserDTO>> getByIds(List<Long> userIds) {
		return R.ok(this.ordinaryUserService.getByIds(userIds));
	}

	@Override
	public R<OrdinaryUserDTO> getById(Long userId) {
		return R.ok(this.ordinaryUserService.getByUserId(userId));
	}

	@Override
	public R<UserInfo> getUserInfo(String username) {
		return this.ordinaryUserService.getUserInfo(username);
	}

	@Override
	public R<List<OrdinaryUserDTO>> queryByMobile(List<String> mobileList) {
		return R.ok(this.ordinaryUserService.queryByMobile(mobileList));
	}

	@Override
	public R<OrdinaryUserDTO> getByMobile(String invitationMobile) {
		return R.ok(ordinaryUserService.getByMobile(invitationMobile));
	}

	@Override
	public R<OrdinaryUserDTO> getUser(String identifier) {
		return R.ok(this.ordinaryUserService.getUser(identifier));
	}

	@Override
	public R<List<Long>> getUserIds(Integer off, Integer size) {
		return R.ok(this.ordinaryUserService.getUserIds(off, size));
	}

	@Override
	public R<PageSupport<OrdinaryUserDTO>> queryAllUser(OrdinaryUserQuery ordinaryUserQuery) {

		return R.ok(ordinaryUserService.queryAllUser(ordinaryUserQuery));
	}

	@Override
	public R<List<Long>> getByLikeMobile(String mobile) {

		return R.ok(ordinaryUserService.getByLikeMobile(mobile));
	}

	@Override
	public R updateStatusByUserName(String username, Boolean status) {
		return R.ok(ordinaryUserService.updateStatusByUserName(username, status));
	}
}
