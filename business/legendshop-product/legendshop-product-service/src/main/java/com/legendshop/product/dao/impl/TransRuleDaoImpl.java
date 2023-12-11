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
import com.legendshop.product.dao.TransRuleDao;
import com.legendshop.product.entity.TransRule;
import org.springframework.stereotype.Repository;

/**
 * 店铺运费规则(TransRule)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-08 17:00:50
 */
@Repository
public class TransRuleDaoImpl extends GenericDaoImpl<TransRule, Long> implements TransRuleDao {

	@Override
	public TransRule getByShopId(Long shopId) {
		return getByProperties(new EntityCriterion().eq("shopId", shopId));
	}
}
