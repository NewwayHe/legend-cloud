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
import com.legendshop.product.dao.DraftProductDao;
import com.legendshop.product.entity.DraftProduct;
import com.legendshop.product.enums.DraftProductStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 商品SPU草稿表(DraftProduct)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-05-08 09:37:10
 */
@Repository
public class DraftProductDaoImpl extends GenericDaoImpl<DraftProduct, Long> implements DraftProductDao {

	@Override
	public DraftProduct getByProductId(Long productId) {
		if (null == productId) {
			return null;
		}

		LambdaEntityCriterion<DraftProduct> criterion = new LambdaEntityCriterion<>(DraftProduct.class);
		criterion.eq(DraftProduct::getProductId, productId);
		criterion.addDescOrder(DraftProduct::getVersion);
		criterion.limit(0, 1);
		return getByProperties(criterion);
	}

	@Override
	public List<DraftProduct> queryByProductId(List<Long> productId) {
		if (CollUtil.isEmpty(productId)) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<DraftProduct> criterion = new LambdaEntityCriterion<>(DraftProduct.class);
		criterion.in(DraftProduct::getProductId, productId);
		return queryByProperties(criterion);
	}

	@Override
	public Integer updateStatus(Long productId, Integer oldStatus, DraftProductStatusEnum newStatus) {
		String sql = "update ls_draft_product set status = ?, update_time = NOW() where product_id = ? and status = ?";
		return update(sql, newStatus.getValue(), productId, oldStatus);
	}

	@Override
	public Integer deleteByProductId(List<Long> productIds) {
		if (CollUtil.isEmpty(productIds)) {
			return 0;
		}
		StringBuilder sql = new StringBuilder("delete from ls_draft_product where product_id in (");
		for (Long productId : productIds) {
			sql.append(productId);
			sql.append(",");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString());
	}

	@Override
	public Integer deleteByProductId(Long productId) {
		String sql = "delete from ls_draft_product where product_id = ?";
		return update(sql, productId);
	}
}
