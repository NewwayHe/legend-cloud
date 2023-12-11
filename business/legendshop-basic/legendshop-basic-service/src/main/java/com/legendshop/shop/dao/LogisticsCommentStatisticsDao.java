/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.shop.entity.LogisticsCommentStatistics;

/**
 * 物流评分统计服务.
 *
 * @author legendshop
 */
public interface LogisticsCommentStatisticsDao extends GenericDao<LogisticsCommentStatistics, Long> {

	/**
	 * 根据店铺ID获取物流评分
	 *
	 * @param shopId 店铺ID
	 * @return
	 */
	LogisticsCommentStatistics getLogisticsCommStatByShopId(Long shopId);


	/**
	 * 根据物流类型获取物流评分
	 *
	 * @param logisticsTemplateId 物流类型ID
	 * @return
	 */
	LogisticsCommentStatistics getLogisticsCommStatByLogisticsTemplateId(Long logisticsTemplateId);

	/**
	 * 保存物流评分
	 *
	 * @param logisticsStatistics 物流类型ID
	 * @return
	 */
	Long saveLogisticeCommStat(LogisticsCommentStatistics logisticsStatistics);

	/**
	 * 更新物流评分
	 *
	 * @param score
	 * @param count
	 * @param logisticsId
	 * @return
	 */
	int updateLogisticeCommStat(Integer score, Integer count, Long logisticsId);
}
