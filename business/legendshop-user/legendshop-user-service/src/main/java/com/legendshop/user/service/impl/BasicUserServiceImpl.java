/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.VerifyCodeDTO;
import com.legendshop.user.dto.VerifyUserDTO;
import com.legendshop.user.service.BasicUserService;
import com.legendshop.user.utils.VerifyCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserServiceImpl implements BasicUserService {

	@Override
	public R<String> verifyCodeExchangeCertificate(VerifyUserDTO userDTO) {
		VerifyCodeDTO verifyCodeDTO = new VerifyCodeDTO(userDTO.getCode(), userDTO.getMobile(), userDTO.getUserType(), userDTO.getCodeType());
		return VerifyCodeUtil.verifyCodeReturnRandomCode(verifyCodeDTO);
	}
}
