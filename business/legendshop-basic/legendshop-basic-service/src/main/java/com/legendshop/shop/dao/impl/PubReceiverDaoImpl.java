/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.shop.dao.PubReceiverDao;
import com.legendshop.shop.entity.PubReceiver;
import com.legendshop.shop.enums.ReceiverEnum;
import org.springframework.stereotype.Repository;

/**
 * 群发消息状态(PubReceiver)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-06-16 11:23:49
 */
@Repository
public class PubReceiverDaoImpl extends GenericDaoImpl<PubReceiver, Long> implements PubReceiverDao {

	@Override
	public PubReceiver getByPubId(Long pubId, Long userId, ReceiverEnum receiverEnum) {
		LambdaEntityCriterion<PubReceiver> criterion = new LambdaEntityCriterion<>(PubReceiver.class);
		criterion.eq(PubReceiver::getPubId, pubId);
		criterion.eq(PubReceiver::getUserId, userId);
		criterion.eq(PubReceiver::getType, receiverEnum.getValue());
		return getByProperties(criterion);
	}

	@Override
	public PubReceiver getDefault(Long pubId, Long userId, ReceiverEnum receiverEnum) {
		PubReceiver pubReceiver = new PubReceiver();
		pubReceiver.setPubId(pubId);
		pubReceiver.setType(receiverEnum.getValue());
		pubReceiver.setUserId(userId);
		pubReceiver.setStatus(0);
		Long id = this.save(pubReceiver);
		pubReceiver.setId(id);
		return pubReceiver;
	}
}
