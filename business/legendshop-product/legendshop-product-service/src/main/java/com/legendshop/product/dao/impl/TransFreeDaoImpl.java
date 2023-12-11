/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.dao.TransFreeDao;
import com.legendshop.product.entity.TransFree;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 条件包邮(TransFree)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-04 16:54:50
 */
@Repository
public class TransFreeDaoImpl extends GenericDaoImpl<TransFree, Long> implements TransFreeDao {

	@Override
	public void delByTransId(Long transId) {
		String sql = "DELETE FROM ls_trans_free WHERE trans_id = ?";
		update(sql, transId);
	}


	@Override
	public List<TransFree> getListByTransId(Long transId) {
		return queryByProperties(new EntityCriterion().eq("transId", transId));
	}
}
