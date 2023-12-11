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
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.product.dto.CartVitLogDTO;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.query.VitLogQuery;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@FeignClient(contextId = "vitLogApi", value = ServiceNameConstants.PRODUCT_SERVICE)
public interface VitLogApi {

	String PREFIX = ServiceNameConstants.PRODUCT_SERVICE_RPC_PREFIX;

	@GetMapping(PREFIX + "/vitLog/userVisitLogCount/{userId}")
	R<Integer> userVisitLogCount(@PathVariable("userId") Long userId);

	@PostMapping(PREFIX + "/vitLog/queryVitListPage")
	R<PageSupport<VitLogDTO>> queryVitListPage(@RequestBody VitLogQuery vitLogQuery);

	@PostMapping(PREFIX + "/vitLog/queryVitList")
	R<List<VitLogDTO>> queryVitList(@RequestParam("userId") Long userId, @RequestParam("shopId") Long shopId);

	@PostMapping(PREFIX + "/queryUserVitList")
	R<PageSupport<VitLogUserHistoryDTO>> queryUserVitList(@RequestParam("pageSize") Integer pageSize, @RequestParam("curPage") Integer curPage);

	@Operation(summary = "【用户端】删除用户足迹（包括历史）")
	@DeleteMapping(PREFIX + "/vitLog/deleteUserVisitLog/{productId}")
	R<Boolean> deleteUserVisitLog(@PathVariable("productId") Long productId);

	@Operation(summary = "【用户端】删除用户所有的足迹")
	@DeleteMapping(PREFIX + "/vitLog/deleteUserAllVisitLog")
	R<Boolean> deleteUserAllVisitLog();

	@PostMapping(PREFIX + "/vitLog/record")
	R<Void> vitLogRecord(@RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response, @RequestBody VitLogRecordDTO vitLogRecordDTO);

	@PostMapping(PREFIX + "vitLog/saveProdCartView")
	R<Void> saveProdCartView(@RequestBody CartVitLogDTO cartVitLogDTO);

	@PostMapping(PREFIX + "vitLog/batchSaveProdCartView")
	R<Void> batchSaveProdCartView(@RequestBody List<CartVitLogDTO> cartVitLogList);
}
