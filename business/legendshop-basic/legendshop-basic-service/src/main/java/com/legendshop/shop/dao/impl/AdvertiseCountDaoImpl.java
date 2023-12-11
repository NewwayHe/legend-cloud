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
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.shop.dao.AdvertiseCountDao;
import com.legendshop.shop.entity.AdvertiseCount;
import com.legendshop.shop.query.AdvertiseQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * (AdvertiseCount)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-27 17:21:40
 */
@Repository
public class AdvertiseCountDaoImpl extends GenericDaoImpl<AdvertiseCount, Long> implements AdvertiseCountDao {

	@Override
	public AdvertiseCount queryFrequency(Date createTime, Long advertiseId, String source) {

		return get("SELECT * FROM ls_advertise_count WHERE create_time=? and advertise_id=? and source=?", AdvertiseCount.class, createTime, advertiseId, source);

	}

	@Override
	public List<AdvertiseCount> queryAdvertiseCount(AdvertiseQuery query) {
		QueryMap map = new QueryMap();
		map.put("advertiseId", query.getId());
		map.put("source", query.getSource());
		map.put("createTime", query.getTime());
		map.put("beginTime", query.getStartTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("AdvertiseCount.getAdvertiseCount", map);
		List<AdvertiseCount> advertiseCount = query(operation.getSql(), AdvertiseCount.class, operation.getParams());
		return advertiseCount;
	}

	@Override
	public List<AdvertiseCount> queryAdvertiseCountList(AdvertiseQuery query) {
		QueryMap map = new QueryMap();
		map.put("advertiseId", query.getId());
		map.put("source", query.getSource());
		map.put("beginTime", query.getStartTime());
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			map.put("endTime", DateUtil.endOfDay(query.getEndTime()));
		}
		SQLOperation operation = this.getSQLAndParams("AdvertiseCount.getAdvertiseCountList", map);
		List<AdvertiseCount> advertiseCountList = query(operation.getSql(), AdvertiseCount.class, operation.getParams());
		return advertiseCountList;
	}


}
