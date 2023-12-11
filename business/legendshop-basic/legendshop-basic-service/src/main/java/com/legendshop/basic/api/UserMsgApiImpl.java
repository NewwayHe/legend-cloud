/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.bo.UserMsgBo;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.service.MessageService;
import com.legendshop.basic.service.ReceiverService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RequiredArgsConstructor
@RestController
public class UserMsgApiImpl implements UserMsgApi {

	private final MessageService messageService;

	private final ReceiverService receiverService;

	@Override
	@Operation(summary = "【用户】系统通知列表未读数量")
	public R<UserMsgBo> userUnreadMsg(@RequestParam(value = "userId") Long userId) {
		return this.messageService.userUnread(userId);
	}

	@Override
	public Boolean isExist(@RequestParam(value = "userId") Long userId, @RequestParam(value = "msgId") Long msgId, @RequestParam(value = "type") Integer type) {
		return receiverService.getByUserIdAndMsgId(userId, msgId, type);
	}

	@Override
	public R<Boolean> saveReceivers(@RequestBody List<ReceiverDTO> receiverDTOS) {
		return receiverService.save(receiverDTOS);
	}
}
