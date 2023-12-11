/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.user;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.api.UserAddressApi;
import com.legendshop.user.api.UserDetailApi;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.dto.UserDetailDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 同步用户地址定时器
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAddressJob {

	private final UserDetailApi userDetailApi;
	private final UserAddressApi userAddressApi;

	/**
	 * 同步用户地址
	 */
	@XxlJob("userAddress")
	public ReturnT<String> userAddress(String param) {

		XxlJobHelper.log("开始同步用户详情地址");

		R<List<UserDetailDTO>> result = userDetailApi.queryByNotAddress();
		if (!result.success()) {
			XxlJobHelper.log("查询用户列表时报，请重试");
			return ReturnT.SUCCESS;
		}
		List<UserDetailDTO> userDetailList = result.getData();

		if (CollUtil.isEmpty(userDetailList)) {
			XxlJobHelper.log("没有需要更新的用户列表");
			return ReturnT.SUCCESS;
		}

		userDetailList.forEach(userDetail -> {
			R<List<UserAddressDTO>> userAddressListResult = userAddressApi.queryByUserId(userDetail.getUserId());
			List<UserAddressDTO> userAddressList = userAddressListResult.getData();

			Optional<UserAddressDTO> optional = Optional.ofNullable(userAddressList).orElse(Collections.emptyList()).stream().findFirst();
			if (optional.isPresent()) {
				UserAddressDTO userAddress = optional.get();
				userDetail.setProvinceId(userAddress.getProvinceId());
				userDetail.setCityId(userAddress.getCityId());
			}
		});

		userDetailApi.update(userDetailList);
		return ReturnT.SUCCESS;
	}
}
