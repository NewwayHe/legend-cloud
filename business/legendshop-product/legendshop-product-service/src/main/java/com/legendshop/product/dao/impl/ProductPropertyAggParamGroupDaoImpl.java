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
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyAggParamGroupBO;
import com.legendshop.product.dao.ProductPropertyAggParamGroupDao;
import com.legendshop.product.entity.ProductPropertyAggParamGroup;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ProductPropertyAggQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ProductPropertyAggParamGroupDaoImpl extends GenericDaoImpl<ProductPropertyAggParamGroup, Long> implements ProductPropertyAggParamGroupDao {

	@Override
	public int getCountByAggId(Long aggId) {
		return get("select count(1) from ls_product_property_agg_param_group where agg_id=? ", Integer.class, aggId);
	}

	@Override
	public PageSupport<ProductPropertyAggParamGroupBO> queryByPage(ProductPropertyAggQuery query) {
		SimpleSqlQuery ssl = new SimpleSqlQuery(ProductPropertyAggParamGroupBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("aggId", query.getId());
		ssl.setSqlAndParameter("ProductPropertyAggParamGroup.queryByAggId", map);
		return querySimplePage(ssl);
	}

	@Override
	public boolean deleteByAggId(Long aggId) {
		return update("delete from ls_product_property_agg_param_group apg where apg.agg_id=?", aggId) > 0;
	}

	@Override
	public List<ParamGroupBO> queryByAggId(List<Long> aggIdList) {
		StringBuffer sb = new StringBuffer("select pp.* ,ppas.agg_id,seq from ls_product_property_agg_param_group ppas,ls_param_group pp ");
		sb.append("where  pp.id=ppas.group_id  and agg_id in ( ");
		for (Long id : aggIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY agg_id,seq");
		return query(sb.toString(), ParamGroupBO.class, aggIdList.toArray());
	}

	@Override
	public List<ParamGroupBO> queryByCategoryId(long id) {
		return query(getSQL("ProductPropertyAggParamGroup.queryByCategoryId"), ParamGroupBO.class, ProductPropertySourceEnum.SYSTEM.value(), id);
	}
}
