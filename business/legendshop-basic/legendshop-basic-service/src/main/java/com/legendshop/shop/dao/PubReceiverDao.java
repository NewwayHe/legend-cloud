/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.shop.entity.PubReceiver;
import com.legendshop.shop.enums.ReceiverEnum;

/**
 * 群发消息状态(PubReceiver)表数据库访问层
 *
 * @author legendshop
 * @since 2021-06-16 11:23:49
 */
public interface PubReceiverDao extends GenericDao<PubReceiver, Long> {

	PubReceiver getByPubId(Long pubId, Long userId, ReceiverEnum receiverEnum);

	PubReceiver getDefault(Long pubId, Long userId, ReceiverEnum receiverEnum);
}
