/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.bo.ProductPropertyAggBO;
import com.legendshop.product.dao.ProductPropertyAggDao;
import com.legendshop.product.entity.ProductPropertyAgg;
import com.legendshop.product.query.ProductPropertyAggQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品类型Dao
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyAggDaoImpl extends GenericDaoImpl<ProductPropertyAgg, Long> implements ProductPropertyAggDao {

	String queryById = "select a.* from ls_product_property_agg a,ls_product_property_agg_specification sp where a.id=sp.agg_id and sp.prop_id=?  " +
			"union all select a.* from ls_product_property_agg a,ls_product_property_agg_param p where a.id=p.agg_id and p.prop_id=?";

	@Override
	public PageSupport<ProductPropertyAgg> queryProductTypePage(ProductPropertyAggQuery productPropertyAggQuery) {
		CriteriaQuery query = new CriteriaQuery(ProductPropertyAgg.class, productPropertyAggQuery.getPageSize(), productPropertyAggQuery.getCurPage());
		query.like("name", productPropertyAggQuery.getName(), MatchMode.ANYWHERE);
		query.addDescOrder("createTime");
		return queryPage(query);
	}

	@Override
	public PageSupport<ProductPropertyAgg> querySimplePage(ProductPropertyAggQuery query) {
		SimpleSqlQuery sql = new SimpleSqlQuery(ProductPropertyAgg.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.like("name", query.getName(), MatchMode.ANYWHERE);
		map.put("id", query.getId());
		switch (query.getSource()) {
			//排除参数id
			case "param":
				sql.setSqlAndParameter("ProductPropertyAgg.queryExceptParamId", map);
				break;
			//排除参数组id
			case "paramGroup":
				sql.setSqlAndParameter("ProductPropertyAgg.queryExceptParamGroupId", map);
				break;
			//排除规格id
			case "specification":
				sql.setSqlAndParameter("ProductPropertyAgg.queryExceptSpecificationId", map);
				break;
			default:
		}
		return querySimplePage(sql);
	}

	@Override
	public List<ProductPropertyAggBO> queryByProductId(Long id) {
		return query(queryById, ProductPropertyAggBO.class, id, id);
	}

	@Override
	public List<ProductPropertyAgg> listAll() {
		return query("select a.id,a.name from ls_product_property_agg a", ProductPropertyAgg.class);
	}

	@Override
	public Long getId(String name) {
		return get("select id from ls_product_property_agg where name=?", Long.class, name);
	}

	@Override
	public Long getCategoryId(Long categoryId) {
		return get("select id from ls_product_property_agg where category_id=?", Long.class, categoryId);
	}

}
