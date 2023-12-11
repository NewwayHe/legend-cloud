/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.product.entity.TransConstFee;

import java.util.List;

/**
 * 固定运费(TransConstFee)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-07 14:43:46
 */
public interface TransConstFeeDao extends GenericDao<TransConstFee, Long> {

	/**
	 * 删除模板下设置的固定运费
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);


	/**
	 * 查询模板下设置的固定运费
	 *
	 * @param transId
	 * @return
	 */
	List<TransConstFee> getListByTransId(Long transId);
}
