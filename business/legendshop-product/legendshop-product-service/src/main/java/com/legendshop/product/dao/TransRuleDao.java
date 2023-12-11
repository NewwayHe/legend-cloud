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
import com.legendshop.product.entity.TransRule;

/**
 * 店铺运费规则(TransRule)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-08 17:00:50
 */
public interface TransRuleDao extends GenericDao<TransRule, Long> {

	/**
	 * 根据店铺id获取
	 *
	 * @param shopId
	 * @return
	 */
	TransRule getByShopId(Long shopId);

}
