/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.shop.dao.LogisticsCommentStatisticsDao;
import com.legendshop.shop.entity.LogisticsCommentStatistics;
import com.legendshop.shop.service.convert.LogisticsCommentStatisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 物流评分统计服务.
 *
 * @author legendshop
 */
@Repository
public class LogisticsCommentStatisticsDaoImpl extends GenericDaoImpl<LogisticsCommentStatistics, Long> implements LogisticsCommentStatisticsDao {

	@Autowired
	private LogisticsCommentStatisticsConverter logisticsCommstatConverter;

	@Override
	public LogisticsCommentStatistics getLogisticsCommStatByShopId(Long shopId) {
		String sql = "SELECT SUM(s.score) AS score,SUM(s.count) AS count FROM ls_logistics_comment_statistics s INNER JOIN  ls_order_logistics t " +
				"ON s.logistics_company_id=t.logistics_company_id LEFT JOIN ls_order o on o.id = t.order_id WHERE o.shop_id=?";
		return this.get(sql, LogisticsCommentStatistics.class, shopId);
	}

	@Override
	public LogisticsCommentStatistics getLogisticsCommStatByLogisticsTemplateId(Long logisticsTemplateId) {
		String sql = "SELECT * FROM ls_logistics_comment_statistics WHERE logistics_company_id = ? FOR UPDATE";
		return this.get(sql, LogisticsCommentStatistics.class, logisticsTemplateId);
	}

	@Override
	public Long saveLogisticeCommStat(LogisticsCommentStatistics logisticsStatistics) {
		return save(logisticsStatistics);
	}

	@Override
	public int updateLogisticeCommStat(Integer score, Integer count, Long logisticsId) {
		String sql = "update ls_logistics_comment_statistics set score = score+?,count = count+? where logistics_company_id=?";
		return update(sql, score, count, logisticsId);
	}

}
