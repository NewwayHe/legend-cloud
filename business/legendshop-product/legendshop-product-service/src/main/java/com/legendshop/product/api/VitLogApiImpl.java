/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.CartVitLogDTO;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.query.VitLogQuery;
import com.legendshop.product.service.VitLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class VitLogApiImpl implements VitLogApi {

	final VitLogService vitLogService;


	@Override
	public R<Integer> userVisitLogCount(Long userId) {
		return vitLogService.userVisitLogCount(userId);
	}

	@Override
	public R<PageSupport<VitLogDTO>> queryVitListPage(VitLogQuery vitLogQuery) {
		return R.ok(vitLogService.queryVitListPage(vitLogQuery));
	}

	@Override
	public R<List<VitLogDTO>> queryVitList(Long userId, Long shopId) {
		return vitLogService.queryVitList(userId, shopId);
	}

	@Override
	public R<PageSupport<VitLogUserHistoryDTO>> queryUserVitList(Integer pageSize, Integer curPage) {
		return R.ok(vitLogService.queryUserVitList(pageSize, curPage));
	}

	@Override
	public R<Boolean> deleteUserVisitLog(Long productId) {
		Long userId = SecurityUtils.getUserId();
		Boolean result = vitLogService.deleteVitLog(userId, productId);
		if (result) {
			return R.ok(true);
		} else {
			return R.fail("删除用户足迹失败!");
		}
	}

	@Override
	public R<Boolean> deleteUserAllVisitLog() {
		Long userId = SecurityUtils.getUserId();
		Boolean result = vitLogService.deleteVitLog(userId, null);
		if (result) {
			return R.ok(true);
		} else {
			return R.fail("删除用户所有足迹失败!");
		}
	}

	@Override
	public R<Void> vitLogRecord(HttpServletRequest request, HttpServletResponse response, VitLogRecordDTO vitLogRecordDTO) {
		// 异步记录浏览历史
		vitLogService.recordVitLog(vitLogRecordDTO);
		return R.ok();
	}

	@Override
	public R<Void> saveProdCartView(CartVitLogDTO cartVitLogDTO) {
		// 异步记录浏览历史
		vitLogService.saveProdCartView(cartVitLogDTO);
		return R.ok();
	}

	@Override
	public R<Void> batchSaveProdCartView(List<CartVitLogDTO> cartVitLogList) {
		// 异步记录浏览历史
		vitLogService.batchSaveProdCartView(cartVitLogList);
		return R.ok();
	}
}
