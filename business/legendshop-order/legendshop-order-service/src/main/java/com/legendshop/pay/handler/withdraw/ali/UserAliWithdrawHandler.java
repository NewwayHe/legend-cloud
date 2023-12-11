/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.withdraw.ali;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.UserWithdrawDTO;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component("ALI_USER_WALLET")
public class UserAliWithdrawHandler extends AbstractAliWithdrawHandler {

	@Override
	protected R<Void> checkUserWithdrawSetting() {
		return null;
	}

	@Override
	protected R<UserWithdrawDTO> checkUserWallet(Long withdrawSerialNo) {
		return null;
	}

	@Override
	protected R<Void> completeWithdrawal(Long withdrawSerialNo) {
		return null;
	}
}
