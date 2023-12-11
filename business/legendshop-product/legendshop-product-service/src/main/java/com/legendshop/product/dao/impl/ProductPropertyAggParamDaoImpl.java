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
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dao.ProductPropertyAggParamDao;
import com.legendshop.product.entity.ProductPropertyAggParam;
import com.legendshop.product.query.ParamGroupQuery;
import com.legendshop.product.query.ProductPropertyAggQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品类型跟参数的关系服务
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyAggParamDaoImpl extends GenericDaoImpl<ProductPropertyAggParam, Long> implements ProductPropertyAggParamDao {

	@Override
	public Boolean deleteByAggId(Long aggId) {
		return update("delete from ls_product_property_agg_param ptp where ptp.agg_id=?", aggId) > 0;
	}

	@Override
	public int getCountByAggId(Long aggId) {
		return get("select count(1) from ls_product_property_agg_param where agg_id=? ", Integer.class, aggId);
	}

	@Override
	public List<ProductPropertyBO> queryByAggId(List<Long> aggIdList) {
		StringBuffer sb = new StringBuffer("select agg_id,pp.prop_name from ls_product_property_agg_param ppas,ls_product_property pp " +
				"where  pp.id=ppas.prop_id and pp.delete_flag=0  and pp.attribute_type=\"P\" and  agg_id in ( ");
		for (Long id : aggIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY ppas.agg_id,ppas.seq");
		return query(sb.toString(), ProductPropertyBO.class, aggIdList.toArray());
	}

	@Override
	public List<ProductPropertyBO> getAggNameByPropId(List<Long> productIdList) {
		StringBuffer sb = new StringBuffer("select a.prop_id as id,b.name as aggPropName from ls_product_property_agg_param a ,ls_product_property_agg b\n" +
				"where a.agg_id=b.id and a.prop_id in (");
		for (Long id : productIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY a.prop_id,a.agg_id");
		return query(sb.toString(), ProductPropertyBO.class, productIdList.toArray());
	}

	@Override
	public List<ProductPropertyAggBO> queryAggNameByGroupId(List<Long> groupIds) {
		StringBuffer sb = new StringBuffer("select group_id,Name ,a.id from ls_product_property_agg a ,ls_product_property_agg_param_group " +
				"b where a.id=b.agg_id and  group_id in(");
		for (Long id : groupIds) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(") ORDER BY group_id,seq");
		return query(sb.toString(), ProductPropertyAggBO.class, groupIds.toArray());
	}

	@Override
	public PageSupport<ProductPropertyBO> queryByPage(ProductPropertyAggQuery query) {
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(ProductPropertyBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("aggId", query.getId());
		sqlQuery.setSqlAndParameter("ProductPropertyAggParam.queryByPage", map);
		return querySimplePage(sqlQuery);
	}

	@Override
	public PageSupport<ProductPropertyBO> getParamAndValueById(ParamGroupQuery query) {
		SimpleSqlQuery sql = new SimpleSqlQuery(ProductPropertyBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("id", query.getId());
		sql.setSqlAndParameter("ProductPropertyParamGroup.getParamAndValueById", map);
		return querySimplePage(sql);
	}

	@Override
	public void deleteByPropId(Long id) {
		update("delete from ls_product_property_agg_param where prop_id=?", id);
	}
}
