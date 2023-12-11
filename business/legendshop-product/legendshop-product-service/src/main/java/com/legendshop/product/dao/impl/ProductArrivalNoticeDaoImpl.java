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
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dao.ProductArrivalNoticeDao;
import com.legendshop.product.entity.ProductArrivalNotice;
import com.legendshop.product.query.ProductArrivalNoticeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 到货通知
 *
 * @author legendshop
 */
@Repository
public class ProductArrivalNoticeDaoImpl extends GenericDaoImpl<ProductArrivalNotice, Long> implements ProductArrivalNoticeDao {


	@Override
	public ProductArrivalNotice getAlreadySaveUser(Long userId, Long skuId, Integer status) {
		String sql = "SELECT * FROM ls_product_arrival_notice WHERE user_id = ? AND sku_id = ?  AND  status = ?";
		return this.get(sql, ProductArrivalNotice.class, userId, skuId, status);
	}

	@Override
	public PageSupport<ProductArrivalNoticeBO> getSelectArrival(ProductArrivalNoticeQuery productArrivalNoticeQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(ProductArrivalNoticeBO.class, productArrivalNoticeQuery.getPageSize(), productArrivalNoticeQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("skuId", productArrivalNoticeQuery.getSkuId());
		map.put("shopId", productArrivalNoticeQuery.getShopId());
		query.setSqlAndParameter("ProductArrivalNotice.getArriInformUserBySkuIdAndWhId", map);
		return querySimplePage(query);
	}

	@Override
	public PageSupport<SkuBO> productPage(ProductArrivalNoticeQuery productArrivalNoticeQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(SkuBO.class, productArrivalNoticeQuery.getPageSize(), productArrivalNoticeQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", productArrivalNoticeQuery.getShopId());
		map.like("productName", productArrivalNoticeQuery.getProductName(), MatchMode.ANYWHERE);
		query.setSqlAndParameter("ProductArrivalNotice.productPage", map);
		return querySimplePage(query);
	}

	@Override
	public List<ProductArrivalNotice> queryBySkuId(List<Long> arricalSkuIdList) {
		StringBuffer sb = new StringBuffer("SELECT id,user_id,shop_id,product_id,sku_id,mobile_phone,status,create_time from ls_product_arrival_notice where status=0 and sku_id in( ");
		for (Long id : arricalSkuIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return query(sb.toString(), ProductArrivalNotice.class, arricalSkuIdList.toArray());
	}

	@Override
	public int updateStatusByIds(List<Long> needUpdateIds, Integer status) {
		if (CollUtil.isEmpty(needUpdateIds)) {
			return 0;
		}
		StringBuilder sb = new StringBuilder("update ls_product_arrival_notice set status = ? where id in (");
		for (Long id : needUpdateIds) {
			sb.append(id);
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), status);
	}
}
