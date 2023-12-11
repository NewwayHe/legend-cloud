/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.service.VitLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Api(tags = "访问记录")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vitLog")
public class VitLogController {

	final VitLogService vitLogService;

	/**
	 * 查询当前用户足迹
	 *
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	@ApiOperation(value = "【用户端】用户足迹查询")
	@PostMapping("/queryUserVitList")
	public R<PageSupport<VitLogUserHistoryDTO>> queryUserVitList(@RequestParam("pageSize") Integer pageSize, @RequestParam("curPage") Integer curPage) {
		return R.ok(vitLogService.queryUserVitList(pageSize, curPage));
	}

	/**
	 * 删除用户足迹
	 * 把用户当前商品的足迹全部删除，包括历史的，用户想要删除足迹（不只是删除当天的）
	 *
	 * @param productId
	 * @return
	 */
	@ApiOperation(value = "【用户端】删除用户单个商品的全部足迹")
	@DeleteMapping("/deleteUserVisitLog/{productId}")
	public R<Boolean> deleteUserVisitLog(@PathVariable("productId") Long productId) {
		Long userId = SecurityUtils.getUserId();
		Boolean result = vitLogService.deleteVitLog(userId, productId);
		if (result) {
			return R.ok(true);
		} else {
			return R.fail("删除用户足迹失败!");
		}
	}

	@ApiOperation(value = "【用户端】删除用户所有的足迹")
	@DeleteMapping("/deleteUserAllVisitLog")
	public R<Boolean> deleteUserAllVisitLog() {
		Long userId = SecurityUtils.getUserId();
		Boolean result = vitLogService.deleteVitLog(userId, null);
		if (result) {
			return R.ok(true);
		} else {
			return R.fail("删除用户所有足迹失败!");
		}
	}
}
