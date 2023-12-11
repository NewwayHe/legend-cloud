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
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dao.ProductPropertyAggBrandDao;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.entity.ProductPropertyAggBrand;
import com.legendshop.product.query.ProductPropertyAggQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The Class ProductTypeBrandDaoImpl.
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyAggBrandDaoImpl extends GenericDaoImpl<ProductPropertyAggBrand, Long> implements ProductPropertyAggBrandDao {

	@Override
	public boolean deleteByAggId(Long aggId) {
		return update("delete from ls_product_property_agg_brand ptb where ptb.agg_id=?", aggId) > 0;
	}

	@Override
	public List<BrandBO> queryBrandByAggId(List<Long> aggIdList) {
		StringBuffer sb = new StringBuffer("select agg_id,b.brand_name from ls_product_property_agg_brand ppab,ls_brand b where b.id=ppab.brand_id " +
				"and b.status=1 and agg_id in ( ");
		for (Long id : aggIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY agg_id,ppab.seq");
		return query(sb.toString(), BrandBO.class, aggIdList.toArray());
	}

	@Override
	public PageSupport<BrandBO> queryByPage(ProductPropertyAggQuery query) {
		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(BrandBO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("aggId", query.getId());
		map.put("brandId", query.getBrandId());
		sqlQuery.setSqlAndParameter("ProductPropertyAggBrand.queryByPage", map);
		return querySimplePage(sqlQuery);
	}

	@Override
	public List<BrandDTO> queryBrandListByCategory(Long id) {
		return query(getSQL("ProductPropertyAggBrand.queryBrandListByCategory"), BrandDTO.class, id);
	}

	@Override
	public Long queryBrandListBrand(Long id) {
		return get("SELECT agg_id ls_product_property_agg_brand where brand_id=?", Long.class, id);
	}

}
