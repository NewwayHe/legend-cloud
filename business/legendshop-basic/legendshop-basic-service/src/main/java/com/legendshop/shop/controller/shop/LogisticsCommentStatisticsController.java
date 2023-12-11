/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.LogisticsCommentStatisticsDTO;
import com.legendshop.shop.service.LogisticsCommentStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物流评分
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/logistics/comment/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LogisticsCommentStatisticsController {

	final LogisticsCommentStatisticsService logisticsCommentStatisticsService;


	@PostMapping("/getByLogisticsTemplateId")
	public R<LogisticsCommentStatisticsDTO> getLogisticsCommStatByLogisticsTemplateId(Long logisticsTemplateId) {
		return logisticsCommentStatisticsService.getLogisticsCommStatByLogisticsTemplateId(logisticsTemplateId);
	}

	@PostMapping("/save")
	public R<Void> saveLogisticsCommStat(LogisticsCommentStatisticsDTO logisticsStatistics) {
		logisticsCommentStatisticsService.saveLogisticsCommStat(logisticsStatistics);
		return R.ok();
	}

	@PostMapping("/update")
	public R<Void> updateLogisticsCommStat(@RequestParam(value = "score") Integer score, @RequestParam(value = "count") Integer count, @RequestParam(value = "logisticsId") Long logisticsId) {
		logisticsCommentStatisticsService.updateLogisticsCommStat(score, count, logisticsId);
		return R.ok();
	}
}
