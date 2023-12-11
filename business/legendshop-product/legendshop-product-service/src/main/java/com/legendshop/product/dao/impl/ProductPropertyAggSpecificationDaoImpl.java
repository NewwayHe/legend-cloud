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
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dao.ProductPropertyAggSpecificationDao;
import com.legendshop.product.entity.ProductPropertyAggSpecification;
import com.legendshop.product.query.ProductPropertyAggQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ProductPropertyAggSpecificationDaoImpl extends GenericDaoImpl<ProductPropertyAggSpecification, Long> implements ProductPropertyAggSpecificationDao {

	@Override
	public boolean deleteByAggId(Long aggId) {
		return update("delete from ls_product_property_agg_specification ptp where ptp.agg_id=?", aggId) > 0;
	}

	@Override
	public int getCountByAggId(Long aggId) {
		return get("select count(1) from ls_product_property_agg_specification where agg_id=? ", Integer.class, aggId);
	}

	@Override
	public List<ProductPropertyBO> queryByAggId(List<Long> aggIdList) {
		StringBuffer sb = new StringBuffer("select agg_id,pp.prop_name from ls_product_property_agg_specification ppas,ls_product_property pp " +
				"where  pp.id=ppas.prop_id and pp.delete_flag=0  and pp.attribute_type=\"S\" and  agg_id in ( ");
		for (Long id : aggIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY ppas.agg_id,ppas.seq");
		return query(sb.toString(), ProductPropertyBO.class, aggIdList.toArray());
	}

	@Override
	public List<ProductPropertyBO> getAggNameByPropId(List<Long> productIdList) {
		StringBuffer sb = new StringBuffer("select a.prop_id as id,b.name as aggPropName from ls_product_property_agg_specification a ,ls_product_property_agg b\n" +
				"where a.agg_id=b.id and a.prop_id in (");
		for (Long id : productIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY a.prop_id,a.agg_id");
		return query(sb.toString(), ProductPropertyBO.class, productIdList.toArray());
	}

	@Override
	public PageSupport<ProductPropertyBO> queryByPage(ProductPropertyAggQuery query) {
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ProductPropertyBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("aggId", query.getId());
		sqlQuery.setSqlAndParameter("ProductPropertyAggSpecification.queryByPage", map);
		return querySimplePage(sqlQuery);
	}

	@Override
	public int deleteByPropId(Long id) {
		return update("delete from ls_product_property_agg_specification where prop_id=?", id);
	}

}
