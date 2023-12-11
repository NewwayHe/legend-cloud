/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.date.DateUtil;
import com.legendshop.basic.dao.MsgDao;
import com.legendshop.basic.dto.MsgDTO;
import com.legendshop.basic.entity.Msg;
import com.legendshop.basic.service.MessageBizService;
import com.legendshop.basic.service.convert.MsgConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
public class MessageBizServiceImpl implements MessageBizService {

	@Autowired
	private MsgDao msgDao;

	@Autowired
	private MsgConverter msgConverter;

	@Override
	public Long saveSystemMessages(MsgDTO msgDTO, Long[] receiverIds, Integer type) {
		msgDTO.setGlobalFlag(Boolean.TRUE);
		msgDTO.setDeleteStatus(0);
		msgDTO.setRecDate(DateUtil.date());
		Msg msg = msgConverter.from(msgDTO);
		msg.setType(msgDTO.getMsgSendTypeEnum().getValue());
		msg.setTitle(msgDTO.getMsgSendTypeEnum().getName());
		return msgDao.saveSystemMessages(msg, receiverIds, type);
	}
}
