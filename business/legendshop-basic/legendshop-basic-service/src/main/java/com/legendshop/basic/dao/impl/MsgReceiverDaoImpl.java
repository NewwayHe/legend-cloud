/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.basic.dao.MsgReceiverDao;
import com.legendshop.basic.entity.MsgReceiver;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息状态Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class MsgReceiverDaoImpl extends GenericDaoImpl<MsgReceiver, Long> implements MsgReceiverDao {

	@Override
	public MsgReceiver getMsgStatus(Long userId, Long msgId) {
		if (null == userId || null == msgId) {
			return null;
		}
		List<MsgReceiver> list = queryByProperties(new EntityCriterion().eq("userId", userId).eq("msgId", msgId));
		if (CollUtil.isEmpty(list)) {
			return null;
		} else {
			return list.iterator().next();
		}
	}

}
