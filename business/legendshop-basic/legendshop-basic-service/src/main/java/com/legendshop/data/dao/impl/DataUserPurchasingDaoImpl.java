/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.data.dao.DataUserPurchasingDao;
import com.legendshop.data.entity.DataUserPurchasing;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (DataUserPurchasing)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-23 15:38:56
 */
@Repository
public class DataUserPurchasingDaoImpl extends GenericDaoImpl<DataUserPurchasing, Long> implements DataUserPurchasingDao {


	@Override
	public DataUserPurchasing getByOrderId(Long orderId) {
		LambdaEntityCriterion<DataUserPurchasing> criterion = new LambdaEntityCriterion<>(DataUserPurchasing.class);
		criterion.eq(DataUserPurchasing::getOrderId, orderId);
		criterion.addDescOrder(DataUserPurchasing::getSource);
		List<DataUserPurchasing> purchasingList = this.queryByProperties(criterion);
		return CollUtil.isEmpty(purchasingList) ? null : purchasingList.get(0);
	}
}
