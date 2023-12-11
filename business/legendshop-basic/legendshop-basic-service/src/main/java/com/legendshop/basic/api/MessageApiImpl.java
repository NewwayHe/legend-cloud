/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.basic.dto.MessagePushDTO;
import com.legendshop.basic.dto.MsgDTO;
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import com.legendshop.basic.enums.MsgSendTypeEnum;
import com.legendshop.basic.service.MessageBizService;
import com.legendshop.basic.service.MessageService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class MessageApiImpl implements MessageApi {

	final MessageService messageService;
	final MessageBizService messageBizService;

	/**
	 * 推送消息
	 *
	 * @param messagePushDTO
	 * @return
	 */
	@Override
	public R push(@RequestBody MessagePushDTO messagePushDTO) {
		return messageService.push(messagePushDTO);
	}

	/**
	 * 发送站内信
	 *
	 * @param receiverId 接收用户ID
	 * @param content
	 * @return
	 */
	@Override
	public R<Void> send(Long adminUserId, Long receiverId, String content) {

		if (ObjectUtil.isNull(receiverId) || StrUtil.isBlank(content)) {
			return R.fail("参数错误，请重试");
		}
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setSendUserId(adminUserId);
		msgDTO.setContent(content);
		msgDTO.setMsgSendTypeEnum(MsgSendTypeEnum.ADMIN_NOTIFY);
		//站内信发送
		messageBizService.saveSystemMessages(msgDTO, new Long[]{receiverId}, MsgReceiverTypeEnum.ORDINARY_USER.value());
		return R.ok();
	}

	@Override
	public R<Void> sendToShop(@RequestParam(value = "adminUserId") Long adminUserId, @RequestParam(value = "receiverId") Long receiverId, @RequestParam(value = "content") String content) {
		if (ObjectUtil.isNull(receiverId) || StrUtil.isBlank(content)) {
			return R.fail("参数错误，请重试");
		}
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setSendUserId(adminUserId);
		msgDTO.setContent(content);
		msgDTO.setMsgSendTypeEnum(MsgSendTypeEnum.ADMIN_NOTIFY);
		//站内信发送
		messageBizService.saveSystemMessages(msgDTO, new Long[]{receiverId}, MsgReceiverTypeEnum.SHOP_USER.value());
		return R.ok();
	}

	@Override
	public R<Void> pushList(@RequestBody List<MessagePushDTO> messagePushDTOList) {
		messagePushDTOList.forEach(e -> {
			messageService.push(e);
		});
		return R.ok();
	}

	@Override
	public R<Boolean> sendToAllAdmin(@RequestParam(value = "content") String content, @RequestParam(value = "detailId", required = false) Long detailId, @RequestParam(value = "msgSendTypeEnum") MsgSendTypeEnum msgSendTypeEnum) {
		Boolean flag = messageService.sendToAllAdmin(content, detailId, msgSendTypeEnum);
		return R.ok(flag);
	}

	@Override
	public R<Void> sendAdmin(Long detailId, Long receiverId, String content) {

		if (ObjectUtil.isNull(receiverId) || StrUtil.isBlank(content)) {
			return R.fail("参数错误，请重试");
		}
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setDetailId(detailId);
		msgDTO.setContent(content);
		msgDTO.setMsgSendTypeEnum(MsgSendTypeEnum.PROD_AUDIT);
		//站内信发送
		messageBizService.saveSystemMessages(msgDTO, new Long[]{receiverId}, MsgReceiverTypeEnum.ADMIN_USER.value());
		return R.ok();
	}
}
