/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.data.dao.UserAmountDao;
import com.legendshop.data.entity.DataUserAmount;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class UserAmountDaoImpl extends GenericDaoImpl<DataUserAmount, Long> implements UserAmountDao {

	@Override
	public void saveUserAmount(DataUserAmount userAmount) {
		save(userAmount);
	}
}
