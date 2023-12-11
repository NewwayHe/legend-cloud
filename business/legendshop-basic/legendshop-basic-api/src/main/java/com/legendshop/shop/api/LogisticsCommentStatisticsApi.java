/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 物流评分
 *
 * @author legendshop
 */
@FeignClient(contextId = "logisticsCommentStatisticsApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface LogisticsCommentStatisticsApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据物流类型获取物流评分
	 *
	 * @param logisticsTemplateId
	 * @return
	 */
	@PostMapping(PREFIX + "/logistics/comment/statistics/getByLogisticsTemplateId")
	R<LogisticsCommentStatisticsDTO> getLogisticsCommStatByLogisticsTemplateId(@RequestParam(value = "logisticsTemplateId") Long logisticsTemplateId);

	/**
	 * 保存物流评分
	 *
	 * @param logisticsStatistics
	 * @return
	 */
	@PostMapping(PREFIX + "/logistics/comment/statistics/save")
	R<Void> saveLogisticsCommStat(@RequestBody LogisticsCommentStatisticsDTO logisticsStatistics);

	/**
	 * 更新物流评分
	 *
	 * @param score
	 * @param count
	 * @param logisticsId
	 * @return
	 */
	@PostMapping(PREFIX + "/logistics/comment/statistics/update")
	R<Void> updateLogisticsCommStat(@RequestParam(value = "score") Integer score, @RequestParam(value = "count") Integer count, @RequestParam(value = "logisticsId") Long logisticsId);
}
