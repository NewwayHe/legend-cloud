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
import com.legendshop.product.entity.TransFree;

import java.util.List;

/**
 * 条件包邮(TransFree)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-04 16:54:50
 */
public interface TransFreeDao extends GenericDao<TransFree, Long> {

	/**
	 * 删除模板下设置的条件包邮
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);

	/**
	 * 查询模板下设置的条件包邮
	 *
	 * @param transId
	 * @return
	 */
	List<TransFree> getListByTransId(Long transId);
}
