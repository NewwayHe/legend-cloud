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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.shop.bo.NewsBO;
import com.legendshop.shop.dao.NewsDao;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.entity.News;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.query.NewsQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 帮助文章Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class NewsDaoImpl extends GenericDaoImpl<News, Long> implements NewsDao {


	@Override
	public List<News> getNewsBycatId(Long catId) {
		return queryByProperties(new EntityCriterion().eq("newsCategoryId", catId));
	}

	@Override
	public List<NewsDTO> getNewsByCatIds(List<Long> catIds) {
		if (CollUtil.isEmpty(catIds)) {
			return Collections.emptyList();
		}

		return queryDTOByProperties(new LambdaEntityCriterion<>(NewsDTO.class).in(NewsDTO::getNewsCategoryId, catIds).eq(NewsDTO::getStatus, CommonConstants.STATUS_NORMAL).addAscOrder(NewsDTO::getSeq));
	}

	/**
	 * 帮助文章的分页查询
	 *
	 * @param newsQuery
	 * @return
	 */
	@Override
	public PageSupport<NewsBO> queryPageList(NewsQuery newsQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(NewsBO.class, newsQuery.getPageSize(), newsQuery.getCurPage());
		QueryMap paramMap = new QueryMap();
		paramMap.like("newsTitle", newsQuery.getNewsTitle(), MatchMode.ANYWHERE);
		paramMap.put("newsCategoryId", newsQuery.getNewsCategoryId());
		paramMap.put("beginTime", newsQuery.getBeginTime());
		if (StrUtil.isNotBlank(newsQuery.getEndTime())) {
			paramMap.put("endTime", DateUtil.endOfDay(DateUtil.parse(newsQuery.getEndTime())));
		}
		paramMap.put("status", newsQuery.getStatus());
		query.setSqlAndParameter("News.queryPageList", paramMap);
		return querySimplePage(query);
	}

	/**
	 * 修改帮助文章状态
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	public int updateStatus(Integer status, Long id) {
		return update("  UPDATE `ls_news` SET `status`=? WHERE id=?", status, id);
	}

	@Override
	public NewsDTO getNewsAndDisPlay(Long id) {
		return this.get("SELECT n.*,nc.display_page as displayPage FROM  ls_news n LEFT JOIN ls_news_cat nc ON n.news_category_id = nc.id where n.id = ?", NewsDTO.class, id);
	}

	@Override
	public PageSupport<NewsDTO> getCatNews(NewsCategoryQuery params) {
		SimpleSqlQuery query = new SimpleSqlQuery(NewsDTO.class, params.getPageSize(), params.getCurPage());
		QueryMap map = new QueryMap();
		List<Integer> displayPageList = new ArrayList<>();
		displayPageList.add(params.getDisplayPage());
		//同时可获取全部端显示的文章
		displayPageList.add(DisplayPageEnum.ALL_DISPLAY.getValue());
		map.put("id", params.getId());
		map.put("displayPageList", displayPageList);
		if (StrUtil.isNotBlank(params.getWord())) {
			map.put("word", params.getWord());
		}
		query.setSqlAndParameter("News.getNewsAndDisPlay", map);
		return querySimplePage(query);
	}

	@Override
	public NewsDTO getNewsByDisplay(Long id, Integer displayPage) {
		return get("select n.id, n.news_title, n.news_brief, n.create_time, n.news_content, nc.display_page from ls_news n left join " +
						"ls_news_cat nc on n.news_category_id = nc.id where n.status = 1 and nc.status = 1 and n.id = ? and nc.display_page in (?,?)"
				, NewsDTO.class, id, displayPage, DisplayPageEnum.ALL_DISPLAY.value());
	}

	@Override
	public PageSupport<NewsDTO> getByWord(NewsCategoryQuery params) {
		SimpleSqlQuery query = new SimpleSqlQuery(NewsDTO.class, params.getPageSize(), params.getCurPage());
		QueryMap map = new QueryMap();
		List<Integer> displayPageList = new ArrayList<>();
		displayPageList.add(params.getDisplayPage());
		//同时可获取全部端显示的文章
		displayPageList.add(DisplayPageEnum.ALL_DISPLAY.getValue());
		if (StrUtil.isNotBlank(params.getWord())) {
			map.like("word", params.getWord(), MatchMode.ANYWHERE);
		}
		if (ObjectUtil.isNotEmpty(params.getId())) {
			map.put("id", params.getId());
		}
		map.put("displayPageList", displayPageList);
		query.setSqlAndParameter("News.getByWord", map);
		return querySimplePage(query);
	}

	@Override
	public List<NewsDTO> getOnlineNews(NewsQuery newsQuery) {
		QueryMap map = new QueryMap();
		map.put("newsCategoryId", newsQuery.getNewsCategoryId());
		map.put("limit", newsQuery.getPageSize());

		SQLOperation operation = this.getSQLAndParams("News.getOnlineNews", map);
		return query(operation.getSql(), NewsDTO.class, operation.getParams());
	}

	@Override
	public List<News> queryNews(List<Long> ids) {
		if (CollUtil.isEmpty(ids)) {
			return Collections.emptyList();
		}
		LambdaEntityCriterion<NewsDTO> criterion = new LambdaEntityCriterion<>(NewsDTO.class);
		criterion.in(NewsDTO::getNewsCategoryId, ids);
		return queryByProperties(criterion);
	}
}
