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
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.user.dto.UserDetailDTO;
import com.legendshop.user.dto.UserInformationDTO;
import com.legendshop.user.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class UserDetailApiImpl implements UserDetailApi {

	final UserDetailService userDetailService;

	@Override
	public R<UserDetailDTO> getUserDetailById(Long userId) {
		return R.ok(this.userDetailService.getUserDetailById(userId));
	}

	@Override
	public R<List<UserDetailDTO>> getUserDetailByIds(List<Long> userIds) {
		return R.ok(this.userDetailService.queryById(userIds));
	}

	@Override
	public R<UserInformationDTO> getUserInfoById(Long userId) {
		return R.ok(this.userDetailService.getUserInfoById(userId));
	}

	@Override
	public R<List<UserInformationDTO>> getUserInfoByIds(List<Long> userIds) {
		return R.ok(this.userDetailService.getUserInfoByIds(userIds));
	}

	@Override
	public R<Void> updateConsumptionStatistics(Long userId, BigDecimal amount, Integer count) {
		int result = this.userDetailService.updateConsumptionStatistics(userId, amount, count);
		if (result > 0) {
			return R.ok();
		}
		return R.fail("更新用户消费统计失败");
	}

	@Override
	public R<Void> updateDistribution(Long userId, Long distributionUserId) {
		return userDetailService.updateDistribution(userId, distributionUserId);
	}

	@Override
	public R<Void> verificationPayPassword(Long userId, String password) {
		return this.userDetailService.verificationPayPassword(userId, password);
	}

	@Override
	public R<Void> feasibilityTest(String param) {
		R<Void> result = this.userDetailService.feasibilityTest(param);
		String a = null;
		a.length();
		if (StringUtils.isNotBlank(param)) {
			throw new BusinessException(" [ User Wallet ] ---> 异常捕获可行性测试！");
		}
		return R.ok();
	}

	@Override
	public R<List<UserDetailDTO>> queryByNotAddress() {
		return R.ok(userDetailService.queryByNotAddress());
	}

	@Override
	public R update(List<UserDetailDTO> userDetailList) {

		userDetailService.updateByList(userDetailList);
		return R.ok();
	}
}
