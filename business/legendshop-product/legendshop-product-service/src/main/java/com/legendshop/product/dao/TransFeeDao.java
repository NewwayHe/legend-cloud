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
import com.legendshop.product.entity.TransFee;

import java.util.List;

/**
 * 运输费用(TransFee)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-04 16:54:47
 */
public interface TransFeeDao extends GenericDao<TransFee, Long> {


	/**
	 * 批量添加
	 *
	 * @param transFees
	 */
	void batchAdd(List<TransFee> transFees);

	/**
	 * 删除模板下设置的运费计算
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);


	/**
	 * 查询模板下设置的运费计算
	 *
	 * @param transId
	 * @return
	 */
	List<TransFee> getListByTransId(Long transId);
}
