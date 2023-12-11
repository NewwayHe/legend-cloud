/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.basic.enums.DecoratePageCategoryEnum;
import com.legendshop.basic.enums.DecoratePageSourceEnum;
import com.legendshop.basic.enums.DecoratePageStatusEnum;
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.shop.dao.DecoratePageDao;
import com.legendshop.shop.entity.DecoratePage;
import com.legendshop.shop.enums.CategoryEnum;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 装修页面(DecoratePage)表数据库访问层实现
 *
 * @author legendshop
 */
@Repository
public class DecoratePageDaoImpl extends GenericDaoImpl<DecoratePage, Long> implements DecoratePageDao {

	@Override
	public PageSupport<DecoratePage> queryPageList(DecoratePageQuery decoratePageQuery) {

		int curPage = decoratePageQuery.getCurPage() == null ? 1 : decoratePageQuery.getCurPage();
		CriteriaQuery cq = new CriteriaQuery(DecoratePage.class, decoratePageQuery.getPageSize(), curPage);
		cq.eq("category", decoratePageQuery.getCategory());
		cq.eq("source", decoratePageQuery.getSource());
		cq.eq("status", decoratePageQuery.getStatus());
		cq.eq("useFlag", decoratePageQuery.getUseFlag());
		cq.eq("type", decoratePageQuery.getType());
		cq.like("name", decoratePageQuery.getName(), MatchMode.ANYWHERE);
		cq.addDescOrder("recDate");
		return queryPage(cq);
	}

	@Override
	public PageSupport<DecoratePage> queryPageListDesc(DecoratePageQuery decoratePageQuery) {
		QueryMap queryMap = new QueryMap();

		queryMap.put("category", decoratePageQuery.getCategory());
		queryMap.put("source", decoratePageQuery.getSource());
		queryMap.put("status", decoratePageQuery.getStatus());
		queryMap.put("useFlag", decoratePageQuery.getUseFlag());
		queryMap.like("name", decoratePageQuery.getName(), MatchMode.ANYWHERE);
		queryMap.put("type", decoratePageQuery.getType());
		int startLimit = (decoratePageQuery.getCurPage() - 1) * decoratePageQuery.getPageSize();
		int endLimit = (decoratePageQuery.getPageSize() * decoratePageQuery.getCurPage() - 1);

		Boolean isPosterOrIndexCategory = CategoryEnum.POSTER_T.getValue().equals(decoratePageQuery.getCategory())
				|| CategoryEnum.INDEX_T.getValue().equals(decoratePageQuery.getCategory());
		Boolean isFirstEmpty = decoratePageQuery.getIsSetFirstEmpty();

		//需要空对象则 减少一个查询对象
		if (ObjectUtil.isNotEmpty(isFirstEmpty)) {
			if (isPosterOrIndexCategory && isFirstEmpty) {
				if (decoratePageQuery.getCurPage() > 1) {
					startLimit -= 1;
				}
				endLimit = (decoratePageQuery.getPageSize() * decoratePageQuery.getCurPage() - 2);
			}
		}

		queryMap.put("limit", "limit " + startLimit + "," + endLimit);
		SQLOperation pageOperation = this.getSQLAndParams("DecoratePage.queryPageListDesc", queryMap);
		SQLOperation totalCountOperation = this.getSQLAndParams("DecoratePage.queryPageListDescCount", queryMap);

		List<DecoratePage> list = this.query(pageOperation.getSql(), DecoratePage.class, pageOperation.getParams());
		Integer totalCount = this.get(totalCountOperation.getSql(), Integer.class, totalCountOperation.getParams());

		PageSupport<DecoratePage> pageSupport = new PageSupport();
		pageSupport.setCurPageNO(decoratePageQuery.getCurPage());
		pageSupport.setPageSize(decoratePageQuery.getPageSize());
		if (CollUtil.isEmpty(list)) {
			list = new ArrayList<DecoratePage>();
		}
		pageSupport.setResultList(list);
		if (ObjectUtil.isEmpty(totalCount)) {
			totalCount = 0;
		}
		pageSupport.setTotal(totalCount);
		int pageCount = totalCount % decoratePageQuery.getCurPage() == 0 ? totalCount / decoratePageQuery.getPageSize() :
				totalCount / decoratePageQuery.getPageSize() + 1;
		//需要空对象则重新计算, 总行数和总页数
		if (ObjectUtil.isNotEmpty(isFirstEmpty)) {
			if (isPosterOrIndexCategory && isFirstEmpty) {
				//第一页需要添加空对象
				if (decoratePageQuery.getCurPage() == 1) {
					list.add(new DecoratePage());
					Collections.swap(list, list.size() - 1, 0);
				}
				totalCount += 1;
				pageCount = totalCount / decoratePageQuery.getPageSize();

				if (totalCount % 10 != 0) {
					pageCount += 1;
				}
			}
		}


		pageSupport.setPageCount(pageCount);
		pageSupport.setPageCount(pageCount);
		return pageSupport;

	}

	@Override
	public boolean updatePageToUnUse(String source, String category) {
		String sql = "UPDATE ls_decorate_page SET use_flag = ? WHERE use_flag = 1 and source = ? AND category = ?";
		this.update(sql, 0, source, category);
		return true;
	}

	@Override
	public DecoratePage getUsedIndexPage(String source) {
		return getByProperties(new EntityCriterion()
				.eq("source", source)
				.eq("useFlag", true)
				.eq("category", DecoratePageCategoryEnum.INDEX.value()));
	}

	@Override
	public DecoratePage getUsedCenterPage(String source) {
		return getByProperties(new EntityCriterion()
				.eq("source", source)
				.eq("useFlag", true)
				.eq("category", DecoratePageCategoryEnum.PERSONAL_CENTER.value()));
	}

	@Override
	public DecoratePage getUsedDistributionCenterPage(String source) {
		return getByProperties(new EntityCriterion()
				.eq("source", source)
				.eq("category", DecoratePageCategoryEnum.DISTRI_CENTER.value()));
	}

	@Override
	public DecoratePage getUsedIndexPag() {
		//防止数据错误导致首页出现错误， 先取第一个， newway
		List<DecoratePage> result = queryByProperties(new EntityCriterion()
				.eq("source", DecoratePageSourceEnum.MOBILE.value())
				.eq("useFlag", true)
				.eq("category", DecoratePageCategoryEnum.INDEX.value()));
		if (CollUtil.isNotEmpty(result)) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public DecoratePage getPosterPageById(Long pageId, String source) {
		return getByProperties(new EntityCriterion()
				.eq("id", pageId)
				.eq("source", source)
				.eq("category", DecoratePageCategoryEnum.POSTER.value())
				.eq("status", DecoratePageStatusEnum.RELEASED.getNum()));
	}
}
