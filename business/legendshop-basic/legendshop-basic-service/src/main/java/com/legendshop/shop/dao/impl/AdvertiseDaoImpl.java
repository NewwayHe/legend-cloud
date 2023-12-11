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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.shop.dao.AdvertiseDao;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.dto.AdvertiseStausCountDTO;
import com.legendshop.shop.entity.Advertise;
import com.legendshop.shop.query.AdvertiseQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (Advertise)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-27 15:23:36
 */
@Repository
public class AdvertiseDaoImpl extends GenericDaoImpl<Advertise, Long> implements AdvertiseDao {
	@Override
	public PageSupport<AdvertiseDTO> queryAdvertisePage(AdvertiseQuery query) {


		SimpleSqlQuery sql = new SimpleSqlQuery(AdvertiseDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.like("title", query.getTitle());
		map.put("status", query.getStatus());
		map.like("source", query.getSource());
		if (ObjectUtil.isNotEmpty(query.getStartTime())) {
			map.put("beginTime", DateUtil.beginOfDay(query.getStartTime()));
		}
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}

		sql.setSqlAndParameter("Advertise.queryAdvertisePage", map);
		return querySimplePage(sql);
	}

	@Override
	public PageSupport<AdvertiseCountDTO> queryAdvertiseDataReport(AdvertiseQuery query) {

		SimpleSqlQuery sql = new SimpleSqlQuery(AdvertiseCountDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		map.put("advertiseId", query.getId());
		map.put("source", query.getSource());
		if (ObjectUtil.isNotEmpty(query.getStartTime())) {
			map.put("beginTime", DateUtil.beginOfDay(query.getStartTime()));
		}
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		sql.setSqlAndParameter("AdvertiseCount.queryAdvertiseDataReport", map);
		return querySimplePage(sql);
	}

	@Override
	public List<Advertise> queryAdvertise(AdvertiseQuery query) {

		return queryByProperties(new LambdaEntityCriterion<>(Advertise.class)
				.le(Advertise::getStartTime, query.getTime())
				.ge(Advertise::getEndTime, query.getTime())
				.eq(Advertise::getStatus, 1)
				.addAscOrder(Advertise::getSeq));
	}

	@Override
	public List<AdvertiseStausCountDTO> queryAdvertiseAll() {
		return query("SELECT count(*) count ,status FROM ls_advertise where 1=1 group by status ", AdvertiseStausCountDTO.class);
	}
}
