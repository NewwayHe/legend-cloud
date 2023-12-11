/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.basic.dao.ReceiverDao;
import com.legendshop.basic.dto.ReceiverDTO;
import com.legendshop.basic.entity.Receiver;
import org.springframework.stereotype.Repository;

/**
 * 消息状态Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class ReceiverDaoImpl extends GenericDaoImpl<Receiver, Long> implements ReceiverDao {


	@Override
	public Receiver getByUserIdAndMsgId(Long userId, Long msgId, Integer type) {

		return getByProperties(new EntityCriterion().eq("userId", userId).eq("businessId", msgId).eq("businessType", type));
	}

	@Override
	public ReceiverDTO getBusinessId(Long msgId) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("BusinessId", msgId);


		SQLOperation operation = getSQLAndParams("Message.getBusinessId", queryMap);
		return get(operation.getSql(), ReceiverDTO.class, operation.getParams());
	}
}
