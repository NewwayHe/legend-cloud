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
import com.legendshop.product.bo.ProductPropertyAggCategoryBO;
import com.legendshop.product.dao.ProductPropertyAggCategoryDao;
import com.legendshop.product.entity.ProductPropertyAggCategory;
import com.legendshop.product.enums.CategoryStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ProductPropertyAggCategoryDaoImpl extends GenericDaoImpl<ProductPropertyAggCategory, Long> implements ProductPropertyAggCategoryDao {

	@Override
	public boolean deleteByAggId(Long aggId) {
		return update("delete from ls_product_property_agg_category apg where apg.agg_id=?", aggId) > 0;
	}

	@Override
	public List<ProductPropertyAggCategoryBO> queryByAggId(List<Long> aggIdList) {
		StringBuffer sb = new StringBuffer("select pp.* ,ppas.agg_id,ppas.seq from ls_product_property_agg_category ppas,ls_category pp ");
		sb.append("where pp.id=ppas.category_id and pp.status=");
		sb.append(CategoryStatusEnum.CATEGORY_ONLINE.getValue());
		sb.append(" and ppas.agg_id in ( ");
		for (Long id : aggIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY ppas.agg_id,ppas.seq");
		return query(sb.toString(), ProductPropertyAggCategoryBO.class, aggIdList.toArray());
	}

	@Override
	public Long queryAggIdByCategoryId(Long categoryId) {
		return get("select agg_id from ls_product_property_agg_category where category_id = ? ", Long.class, categoryId);
	}

	@Override
	public ProductPropertyAggCategory getByCategoryId(Long categoryId) {
		return getByProperties(new LambdaEntityCriterion<>(ProductPropertyAggCategory.class).eq(ProductPropertyAggCategory::getCategoryId, categoryId));
	}

	@Override
	public void deleteByCategoryId(List<Long> categoryIds) {
		if (CollUtil.isNotEmpty(categoryIds)) {
			StringBuffer sb = new StringBuffer();
			sb.append("delete from ls_product_property_agg_category apg where apg.category_id in (");
			for (Long categoryId : categoryIds) {
				sb.append(categoryId).append(",");
			}
			sb.setLength(sb.length() - 1);
			sb.append(")");
			update(sb.toString());
		}
	}
}
