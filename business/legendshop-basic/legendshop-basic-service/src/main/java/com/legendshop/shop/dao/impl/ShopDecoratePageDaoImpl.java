/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.shop.dao.ShopDecoratePageDao;
import com.legendshop.shop.entity.ShopDecoratePage;
import com.legendshop.shop.query.ShopDecoratePageQuery;
import org.springframework.stereotype.Repository;

/**
 * 移动端店铺主页装修Dao实现类
 *
 * @author legendshop
 */
@Repository
public class ShopDecoratePageDaoImpl extends GenericDaoImpl<ShopDecoratePage, Long> implements ShopDecoratePageDao {


	@Override
	public PageSupport<ShopDecoratePage> queryPageList(ShopDecoratePageQuery shopDecoratePageQuery) {

		CriteriaQuery cq = new CriteriaQuery(ShopDecoratePage.class, shopDecoratePageQuery.getPageSize(), shopDecoratePageQuery.getCurPage());
		cq.eq("shopId", shopDecoratePageQuery.getShopId());
		cq.eq("category", shopDecoratePageQuery.getCategory());
		cq.eq("source", shopDecoratePageQuery.getSource());
		cq.eq("status", shopDecoratePageQuery.getStatus());
		cq.eq("useFlag", shopDecoratePageQuery.getUseFlag());
		cq.like("name", shopDecoratePageQuery.getName(), MatchMode.ANYWHERE);
		cq.addDescOrder("recDate");
		return queryPage(cq);
	}

	@Override
	public PageSupport<ShopDecoratePage> queryPageListDesc(ShopDecoratePageQuery shopDecoratePageQuery) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("shopId", shopDecoratePageQuery.getShopId());
		queryMap.put("category", shopDecoratePageQuery.getCategory());
		queryMap.put("source", shopDecoratePageQuery.getSource());
		queryMap.put("status", shopDecoratePageQuery.getStatus());
		queryMap.put("useFlag", shopDecoratePageQuery.getUseFlag());
		queryMap.like("name", shopDecoratePageQuery.getName(), MatchMode.ANYWHERE);
		queryMap.put("type", shopDecoratePageQuery.getType());
		queryMap.put("defaultFlag", shopDecoratePageQuery.getDefaultFlag());
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(ShopDecoratePage.class, shopDecoratePageQuery);


		simpleSqlQuery.setSqlAndParameter("ShopDecoratePage.queryPageListDesc", queryMap);
		return querySimplePage(simpleSqlQuery);


	}

	@Override
	public boolean updatePageToUnUse(Long shopId, String source) {
		String sql = "UPDATE ls_shop_decorate_page SET use_flag = ? WHERE shop_id=? AND source = ? AND use_flag = 1";
		return this.update(sql, 0, shopId, source) > 0;
	}

	@Override
	public boolean updateDefaultPage(Long shopId, String source, boolean defaultFlag) {
		String sql = "UPDATE ls_shop_decorate_page SET default_flag = ? WHERE shop_id=? AND source = ? ";
		return this.update(sql, defaultFlag, shopId, source) > 0;
	}

	@Override
	public ShopDecoratePage getUsedIndexPage(Long shopId, String source) {
		return getByProperties(new EntityCriterion()
				.eq("shopId", shopId)
				.eq("source", source)
				.eq("useFlag", true)
				.eq("category", DecoratePageCategoryEnum.INDEX.value()));
	}

	@Override
	public ShopDecoratePage getPosterPageById(Long shopId, Long pageId, String source) {
		return getByProperties(new EntityCriterion()
				.eq("id", pageId)
				.eq("shopId", shopId)
				.eq("source", source)
				.eq("category", DecoratePageCategoryEnum.POSTER.value())
				.eq("status", DecoratePageStatusEnum.RELEASED.getNum()));
	}

	@Override
	public ShopDecoratePage getByPageIdAndShopId(Long pageId, Long shopId) {
		return getByProperties(new LambdaEntityCriterion<>(ShopDecoratePage.class)
				.eq(ShopDecoratePage::getShopId, shopId)
				.eq(ShopDecoratePage::getId, pageId));
	}
}
