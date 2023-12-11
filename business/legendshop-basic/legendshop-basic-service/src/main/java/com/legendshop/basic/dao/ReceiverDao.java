/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;


import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.entity.Receiver;

/**
 * 消息已读Dao.
 *
 * @author legendshop
 */
public interface ReceiverDao extends GenericDao<Receiver, Long> {

	Receiver getByUserIdAndMsgId(Long userId, Long msgId, Integer type);

	ReceiverDTO getBusinessId(Long msgId);
}
