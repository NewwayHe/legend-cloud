/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.dao.ProductGroupDao;
import com.legendshop.product.entity.ProductGroup;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductGroupQuery;
import com.legendshop.product.query.ProductGroupRelationQuery;
import org.springframework.stereotype.Repository;

/**
 * The Class ProductGroupDaoImpl. Dao实现类
 *
 * @author legendshop
 */
@Repository
public class ProductGroupDaoImpl extends GenericDaoImpl<ProductGroup, Long> implements ProductGroupDao {

	@Override
	public PageSupport<ProductGroup> queryProductGroupPage(ProductGroupQuery productGroupQuery) {

		int curPage = productGroupQuery.getCurPage() == null ? 1 : productGroupQuery.getCurPage();
		CriteriaQuery query = new CriteriaQuery(ProductGroup.class, productGroupQuery.getPageSize(), curPage);
		query.like("name", productGroupQuery.getName().trim(), MatchMode.ANYWHERE);

		query.addAscOrder("type");
		query.addDescOrder("createTime");
		return queryPage(query);
	}

	@Override
	public PageSupport<ProductGroup> page(ProductGroupQuery productGroupQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(ProductGroup.class, productGroupQuery.getPageSize(), productGroupQuery.getCurPage());
		criteriaQuery.like("name", productGroupQuery.getName(), MatchMode.ANYWHERE);
		criteriaQuery.eq("type", productGroupQuery.getType());
		return this.queryPage(criteriaQuery);
	}

	@Override
	public PageSupport<ProductGroupRelationBO> queryProductList(ProductGroupRelationQuery relationQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductGroupRelationBO.class, relationQuery.getPageSize(), relationQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("groupId", relationQuery.getGroupId());
		map.like("productName", relationQuery.getProductName(), MatchMode.ANYWHERE);
		map.like("shopName", relationQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("status", ProductStatusEnum.PROD_ONLINE.getValue());
		map.put("opStatus", OpStatusEnum.PASS.getValue());
		map.put("delStatus", ProductDelStatusEnum.PROD_NORMAL.getValue());

		if (ObjectUtil.isNotEmpty(relationQuery.getSort())) {
			String descending = " desc";
			if (relationQuery.getDescending() != null && !relationQuery.getDescending()) {
				descending = " asc";
			}
			StringBuffer buffer = new StringBuffer();
			buffer.append("p.").append(relationQuery.getSort());
			map.put("orderBy", "order by " + buffer + descending);
		}
		query.setSqlAndParameter("ProductGroup.queryProductList", map);
		return querySimplePage(query);
	}

}
