/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.product.dao.DraftSkuDao;
import com.legendshop.product.entity.DraftSku;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 单品SKU草稿表(DraftSku)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-05-08 09:37:05
 */
@Repository
public class DraftSkuDaoImpl extends GenericDaoImpl<DraftSku, Long> implements DraftSkuDao {

	@Override
	public List<DraftSku> getSkuByProductId(Long productId) {

		if (null == productId) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<DraftSku> criterion = new LambdaEntityCriterion<>(DraftSku.class);
		criterion.eq(DraftSku::getProductId, productId);

		return queryByProperties(criterion);
	}

	@Override
	public Integer deleteByProductId(Long productId) {
		String sql = "delete from ls_draft_sku where product_id = ?";
		return update(sql, productId);
	}

	@Override
	public Integer deleteByProductId(List<Long> productIds) {
		if (CollUtil.isEmpty(productIds)) {
			return 0;
		}
		StringBuilder sql = new StringBuilder("delete from ls_draft_sku where product_id in (");
		for (Long productId : productIds) {
			sql.append(productId);
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString());
	}
}
