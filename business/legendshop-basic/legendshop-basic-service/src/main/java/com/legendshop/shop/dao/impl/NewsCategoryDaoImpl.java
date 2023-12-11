/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.shop.dao.NewsCategoryDao;
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.entity.NewsCategory;
import com.legendshop.shop.query.NewsCategoryQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章分类Dao.
 *
 * @author legendshop
 */
@Repository
public class NewsCategoryDaoImpl extends GenericDaoImpl<NewsCategory, Long> implements NewsCategoryDao {


	/**
	 * 帮助栏目分页查询
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	@Override
	public PageSupport<NewsCategory> page(NewsCategoryQuery newsCategoryQuery) {
		CriteriaQuery cq = new CriteriaQuery(NewsCategory.class, newsCategoryQuery.getPageSize(), newsCategoryQuery.getCurPage());
		cq.like("newsCategoryName", newsCategoryQuery.getNewsCategoryName(), MatchMode.ANYWHERE);
		cq.eq("status", newsCategoryQuery.getStatus());
		if (StrUtil.isNotBlank(newsCategoryQuery.getEndTime())) {
			cq.between("createTime", newsCategoryQuery.getBeginTime(),
					DateUtil.endOfDay(DateUtil.parse(newsCategoryQuery.getEndTime())));
		}
		cq.addAscOrder("seq");
		return queryPage(cq);
	}

	/**
	 * 修改帮助栏目
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	public int updateStatus(Integer status, Long id) {
		return update("UPDATE `ls_news_cat`  SET  `status`=? WHERE id=?", status, id);
	}

	@Override
	public List<NewsCategory> queryByDisplayPage(Integer displayPage) {
		EntityCriterion cq = new EntityCriterion(true);
		//获取对应端的帮助栏目，和全部端的帮助栏目
		List<Integer> displayPageList = new ArrayList<>();
		displayPageList.add(displayPage);
		if (displayPage != null && !DisplayPageEnum.ALL_DISPLAY.getValue().equals(displayPage)) {
			displayPageList.add(DisplayPageEnum.ALL_DISPLAY.getValue());
		}
		cq.in("displayPage", displayPageList);
		cq.eq("status", CommonConstants.STATUS_NORMAL);
		cq.addAscOrder("seq");
		return queryByProperties(cq);
	}

	@Override
	public List<NewsCategory> queryUseAndAll(Integer displayPage, String word) {
		EntityCriterion cq = new EntityCriterion(true);

		//获取对应端的帮助栏目，和全部端的帮助栏目
		Object[] displayPageList = {displayPage, DisplayPageEnum.ALL_DISPLAY.getValue()};
		cq.in("displayPage", displayPageList);
		cq.eq("status", CommonConstants.STATUS_NORMAL);
		cq.like("newsCategoryName", word, MatchMode.ANYWHERE);
		cq.addAscOrder("seq");
		return queryByProperties(cq);
	}

	@Override
	public List<NewsCategoryDTO> queryNewsCategoryTree(NewsCategoryQuery query) {
		QueryMap map = new QueryMap();
		map.put("displayPage", query.getDisplayPage());
		map.put("status", query.getStatus());
		map.put("limit", query.getPageSize());
		SQLOperation operation = this.getSQLAndParams("NewsCategory.queryNewsCategoryTree", map);
		return this.query(operation.getSql(), NewsCategoryDTO.class, operation.getParams());
	}

	@Override
	public PageSupport<NewsCategory> queryUsePage(NewsCategoryQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(NewsCategory.class, query.getPageSize(), query.getCurPage());
		//获取对应端的帮助栏目，和全部端的帮助栏目
		Object[] displayPageList = {query.getDisplayPage(), DisplayPageEnum.ALL_DISPLAY.getValue()};
		criteriaQuery.in("displayPage", displayPageList);
		criteriaQuery.eq("status", CommonConstants.STATUS_NORMAL);
		criteriaQuery.like("newsCategoryName", query.getWord(), MatchMode.ANYWHERE);
		criteriaQuery.addAscOrder("seq");
		return super.queryPage(criteriaQuery);
	}

	@Override
	public PageSupport<NewsCategoryDTO> queryNewsCategoryPage(NewsCategoryQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(NewsCategoryDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		simpleSqlQuery.setSqlAndParameter("NewsCategory.queryNewsCategoryPage", map);
		PageSupport<NewsCategoryDTO> pageSupport = querySimplePage(simpleSqlQuery);
		return pageSupport;

	}


}
