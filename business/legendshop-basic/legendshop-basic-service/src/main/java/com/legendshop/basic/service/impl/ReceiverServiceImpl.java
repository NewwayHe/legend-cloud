/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.dao.ReceiverDao;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.entity.Receiver;
import com.legendshop.basic.service.ReceiverService;
import com.legendshop.basic.service.convert.ReceiverConverter;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j
public class ReceiverServiceImpl extends BaseServiceImpl<ReceiverDTO, ReceiverDao, ReceiverConverter> implements ReceiverService {

	@Autowired
	ReceiverDao receiverDao;

	@Override
	public boolean getByUserIdAndMsgId(Long userId, Long msgId, Integer type) {
		Receiver receiver = receiverDao.getByUserIdAndMsgId(userId, msgId, type);
		return ObjectUtil.isNotEmpty(receiver);
	}

	@Override
	public ReceiverDTO getBusinessId(Long msgId) {
		return receiverDao.getBusinessId(msgId);
	}
}
