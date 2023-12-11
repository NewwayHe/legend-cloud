/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.data.dao.BaiduViewDao;
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.entity.BaiduView;
import com.legendshop.data.query.BaiduViewQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 百度（移动）统计记录(BaiduView)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-06-19 13:54:05
 */
@Repository
public class BaiduViewDaoImpl extends GenericDaoImpl<BaiduView, Long> implements BaiduViewDao {

	@Override
	public BaiduView getByArchiveTime(String archiveTime) {
		LambdaEntityCriterion<BaiduView> criterion = new LambdaEntityCriterion<>(BaiduView.class);
		criterion.eq(BaiduView::getArchiveTime, archiveTime);
		return getByProperties(criterion);
	}

	@Override
	public BaiduView getAllByArchiveTime(Date startTime, Date endTime) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("startTime", DateUtil.formatDate(startTime));
		queryMap.put("endTime", DateUtil.formatDate(endTime));

		SQLOperation operation = this.getSQLAndParams("BaiduView.getAllByArchiveTime", queryMap);
		return this.get(operation.getSql(), BaiduView.class, operation.getParams());
	}

	@Override
	public List<BaiduViewDTO> getFlowPic(BaiduViewQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("startTime", DateUtil.formatDate(DateUtil.parseDate(query.getStartDate())));
		queryMap.put("endTime", DateUtil.formatDate(DateUtil.parseDate(query.getEndDate())));
		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			queryMap.put("orderBy", " order by " + query.getProp() + " " + query.getOrder());
		}

		SQLOperation operation = this.getSQLAndParams("BaiduView.getFlowPic", queryMap);
		return this.query(operation.getSql(), BaiduViewDTO.class, operation.getParams());
	}

	@Override
	public PageSupport<BaiduViewDTO> getFlowPage(BaiduViewQuery query) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("startTime", DateUtil.formatDate(DateUtil.parseDate(query.getStartDate())));
		queryMap.put("endTime", DateUtil.formatDate(DateUtil.parseDate(query.getEndDate())));
		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			queryMap.put("orderBy", " order by " + StrUtil.toUnderlineCase(query.getProp()) + " " + query.getOrder());
		}

		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(BaiduViewDTO.class, query);
		sqlQuery.setSqlAndParameter("BaiduView.getFlowPic", queryMap);
		return querySimplePage(sqlQuery);
	}
}
