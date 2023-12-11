/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;

/**
 * 物流评分统计表.
 *
 * @author legendshop
 */
public interface LogisticsCommentStatisticsService {

	/**
	 * 根据店铺ID获取物流评分
	 *
	 * @param shopId
	 * @return
	 */
	LogisticsCommentStatisticsBO getLogisticsCommStatByShopId(Long shopId);

	/**
	 * 更新物流评分
	 *
	 * @param score
	 * @param count
	 * @param logisticsId
	 * @return
	 */
	int updateLogisticsCommStat(Integer score, Integer count, Long logisticsId);

	/**
	 * 保存物流评分
	 *
	 * @param logisticsCommentStatisticsDTO
	 * @return
	 */
	Long saveLogisticsCommStat(LogisticsCommentStatisticsDTO logisticsCommentStatisticsDTO);


	/**
	 * 根据物流类型获取物流评分
	 *
	 * @param logisticsTemplateId
	 * @return
	 */
	R<LogisticsCommentStatisticsDTO> getLogisticsCommStatByLogisticsTemplateId(Long logisticsTemplateId);

	/**
	 * 保存物流评论统计信息
	 *
	 * @param logisticsStatistics 物流评论统计信息DTO
	 */
	void saveLogisticeCommStat(LogisticsCommentStatisticsDTO logisticsStatistics);

	/**
	 * 更新物流评论统计信息
	 *
	 * @param logisticsStatistics 物流评论统计信息DTO
	 */
	void updateLogisticeCommStat(LogisticsCommentStatisticsDTO logisticsStatistics);

}
