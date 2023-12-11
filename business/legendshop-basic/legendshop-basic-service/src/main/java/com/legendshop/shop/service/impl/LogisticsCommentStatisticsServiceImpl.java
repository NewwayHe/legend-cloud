/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.dao.LogisticsCommentStatisticsDao;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;
import com.legendshop.shop.service.LogisticsCommentStatisticsService;
import com.legendshop.shop.service.convert.LogisticsCommentStatisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 物流评分统计表服务.
 *
 * @author legendshop
 */
@Service
public class LogisticsCommentStatisticsServiceImpl implements LogisticsCommentStatisticsService {

	@Autowired
	private LogisticsCommentStatisticsDao logisticsCommentStatisticsDao;

	@Autowired
	private LogisticsCommentStatisticsConverter logisticsCommentStatisticsConverter;


	@Override
	public LogisticsCommentStatisticsBO getLogisticsCommStatByShopId(Long shopId) {
		return logisticsCommentStatisticsConverter.convertLogisticsCommStatBO(logisticsCommentStatisticsDao.getLogisticsCommStatByShopId(shopId));
	}


	@Override
	public int updateLogisticsCommStat(Integer score, Integer count, Long logisticsId) {
		return logisticsCommentStatisticsDao.updateLogisticeCommStat(score, count, logisticsId);
	}

	@Override
	public Long saveLogisticsCommStat(LogisticsCommentStatisticsDTO logisticsCommentStatisticsDTO) {
		return logisticsCommentStatisticsDao.save(logisticsCommentStatisticsConverter.from(logisticsCommentStatisticsDTO));
	}

	@Override
	public R<LogisticsCommentStatisticsDTO> getLogisticsCommStatByLogisticsTemplateId(Long logisticsTemplateId) {
		return R.ok(logisticsCommentStatisticsConverter.to(logisticsCommentStatisticsDao.getLogisticsCommStatByLogisticsTemplateId(logisticsTemplateId)));
	}

	@Override
	public void saveLogisticeCommStat(LogisticsCommentStatisticsDTO logisticsStatistics) {
		logisticsCommentStatisticsDao.save(logisticsCommentStatisticsConverter.from(logisticsStatistics));
	}

	@Override
	public void updateLogisticeCommStat(LogisticsCommentStatisticsDTO logisticsStatistics) {
		logisticsCommentStatisticsDao.update(logisticsCommentStatisticsConverter.from(logisticsStatistics));
	}


}
