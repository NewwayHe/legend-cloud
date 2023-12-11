/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.dto.MsgDTO;
import com.legendshop.basic.dto.MsgSendDTO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.service.MessageBizService;
import com.legendshop.basic.service.MessageHandler;
import com.legendshop.common.core.config.sys.params.MsgSendConfig;
import com.legendshop.common.core.expetion.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author legendshop
 */
@Component("systemMessage")
public class MessageSystemHandler implements MessageHandler {

	@Autowired
	private MessageBizService messageBizService;

	@Override
	public void handleMessage(MsgSendDTO msgSendDTO, MsgSendConfig msgSendConfig) {

		if (ObjectUtil.isNull(msgSendConfig.getSystemMsgEnabled())) {
			return;
		}
		Boolean flag = msgSendConfig.getSystemMsgEnabled();
		if (!flag) {
			return;
		}
		MsgDTO msgDTO = new MsgDTO();
		msgDTO.setSendUserId(msgSendDTO.getUserId());
		msgDTO.setContent(msgSendConfig.getSystemMsgContent());
		msgDTO.setMsgSendTypeEnum(msgSendDTO.getMsgSendType());
		msgDTO.setDetailId(msgSendDTO.getDetailId());
		msgDTO.setDataDTOList(msgSendDTO.getMsgSendParamDTOList());
		List<MsgSendParamDTO> dataDTOList = msgDTO.getDataDTOList();
		if (CollUtil.isNotEmpty(dataDTOList)) {
			dataDTOList.forEach(m -> {
				msgDTO.setContent(msgDTO.getContent().replaceAll(m.getMsgSendParam().value(), m.getValue()));
			});
		}

		//站内信发送
		msgSendDTO.getSysMsgReceiverDTOS().forEach(r -> {
			if (r.getReceiveUserIds() == null || r.getReceiveUserIds().length == 0) {
				throw new BusinessException("接收者id不能为空！");
			}
			messageBizService.saveSystemMessages(msgDTO, r.getReceiveUserIds(), r.getReceiverType());
		});

	}
}
