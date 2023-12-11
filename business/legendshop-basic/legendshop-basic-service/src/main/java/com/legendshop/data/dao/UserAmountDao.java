/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.data.entity.DataUserAmount;

/**
 * @author legendshop
 */
public interface UserAmountDao extends GenericDao<DataUserAmount, Long> {

	/**
	 * 保存用户统计统计
	 *
	 * @param userAmount
	 */
	void saveUserAmount(DataUserAmount userAmount);
}
