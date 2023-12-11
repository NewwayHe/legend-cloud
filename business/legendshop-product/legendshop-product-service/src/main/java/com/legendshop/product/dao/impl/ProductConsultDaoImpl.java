/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import com.legendshop.product.dao.ProductConsultDao;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.entity.ProductConsult;
import com.legendshop.product.query.ProductConsultQuery;
import org.springframework.stereotype.Repository;

/**
 * @author legendshop
 */
@Repository
public class ProductConsultDaoImpl extends GenericDaoImpl<ProductConsult, Long> implements ProductConsultDao {

	@Override
	public PageSupport<ProductConsultDTO> queryUserProductConsultPage(ProductConsultQuery productConsultQuery) {
		LambdaCriteriaQuery<ProductConsultDTO> query = new LambdaCriteriaQuery<>(ProductConsultDTO.class, productConsultQuery);
		query.eq(ProductConsultDTO::getProductId, productConsultQuery.getProductId())
				.eq(ProductConsultDTO::getDelSts, 0)
				//查看已回复的商品咨询
				.eq(ProductConsultDTO::getReplySts, 1)
				//查看上线的商品咨询
				.eq(ProductConsultDTO::getStatus, 1)
				.like(ProductConsultDTO::getContent, productConsultQuery.getContent(), MatchMode.ANYWHERE)
				.addDescOrder(ProductConsultDTO::getRecDate);
		return queryDTOPage(query);
	}


	@Override
	public PageSupport<ProductConsultDTO> queryProductConsultPage(ProductConsultQuery productConsultQuery) {
		QueryMap map = new QueryMap();
		map.like("content", productConsultQuery.getContent(), MatchMode.ANYWHERE);
		map.like("name", productConsultQuery.getProductName(), MatchMode.ANYWHERE);
		map.like("shopName", productConsultQuery.getShopName(), MatchMode.ANYWHERE);
		map.put("askUserId", productConsultQuery.getAskUserId());
		map.put("delSts", productConsultQuery.getDelSts());
		map.put("status", productConsultQuery.getStatus());
		map.put("replySts", productConsultQuery.getReplySts());
		if (StrUtil.isNotEmpty(productConsultQuery.getStartDate()) && StrUtil.isNotEmpty(productConsultQuery.getEndDate())) {
			map.put("endDate", DateUtil.endOfDay(DateUtil.parseDate(productConsultQuery.getEndDate())));
			map.put("startDate", DateUtil.beginOfDay(DateUtil.parseDate(productConsultQuery.getStartDate())));
		}
		map.put("shopId", productConsultQuery.getShopId());

		SimpleSqlQuery query = new SimpleSqlQuery(ProductConsultDTO.class, productConsultQuery);
		query.setSqlAndParameter("ProductConsult.queryProductConsultPage", map);
		return querySimplePage(query);
	}
}
