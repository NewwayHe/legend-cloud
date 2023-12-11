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
import com.legendshop.basic.entity.MsgReceiver;

/**
 * 消息状态Dao.
 *
 * @author legendshop
 */
public interface MsgReceiverDao extends GenericDao<MsgReceiver, Long> {
	MsgReceiver getMsgStatus(Long userId, Long msgId);
}
