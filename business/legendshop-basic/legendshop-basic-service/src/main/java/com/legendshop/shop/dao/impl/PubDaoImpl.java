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
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.criterion.Restrictions;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.shop.dao.PubDao;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.entity.Pub;
import com.legendshop.shop.query.PubQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 公告Dao
 *
 * @author legendshop
 */
@Repository
public class PubDaoImpl extends GenericDaoImpl<Pub, Long> implements PubDao {


	@Override
	public PageSupport<Pub> getPubListPage(PubQuery pubQuery) {
		SimpleSqlQuery query = new SimpleSqlQuery(Pub.class, pubQuery.getPageSize(), pubQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.like("title", pubQuery.getTitle(), MatchMode.ANYWHERE);
		map.put("type", pubQuery.getType());
		map.put("status", pubQuery.getStatus());
		query.setSqlAndParameter("Pub.getPubList", map);
		return querySimplePage(query);
	}

	@Override
	public Pub getNewestPubByType(Integer type) {

		Date today = new Date();
		EntityCriterion cq = new EntityCriterion();
		cq.eq("status", CommonConstants.STATUS_NORMAL);
		cq.eq("type", type);
		// 有效期检查
		cq.add(cq.or(Restrictions.ge("endTime", today), Restrictions.isNull("endTime")));
		cq.add(cq.or(Restrictions.le("startTime", today), Restrictions.isNull("startTime")));
		cq.addOrder("desc", "id");
		return queryByProperties(cq, 0, 1).get(0);
	}

	@Override
	public PageSupport<PubDTO> queryPubPageListByType(PubQuery pubQuery) {

		QueryMap queryMap = new QueryMap();
		queryMap.put("userId", pubQuery.getUserId());
		queryMap.put("receiverType", pubQuery.getReceiverType());
		queryMap.put("type", pubQuery.getType());
		queryMap.put("status", CommonConstants.STATUS_NORMAL);
		queryMap.put("startTime", DateUtil.date());
		queryMap.put("endTime", DateUtil.date());

		SimpleSqlQuery sqlQuery = new SimpleSqlQuery(PubDTO.class, pubQuery);
		sqlQuery.setSqlAndParameter("Pub.queryPubPageListByType", queryMap);
		return querySimplePage(sqlQuery);

	}

	@Override
	public Integer userUnreadMsg(Long userId) {
		return get("SELECT COUNT(a.id) FROM (SELECT lp.id, IFNULL(lr.status, 0) AS  receiverStatus FROM ls_pub lp LEFT JOIN ls_pub_receiver lr ON lp.id = lr.pub_id AND lr.user_id = ? AND lr.type = 1 WHERE lp.start_time <= ? AND lp.end_time >= ? AND lp.status = 1 AND lp.type = 0) a WHERE a.receiverStatus = 0", Integer.class, userId, DateUtil.date(), DateUtil.date());
	}
}
