/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.controller.admin;


import cn.hutool.core.util.EnumUtil;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.search.dto.IndexCountDTO;
import com.legendshop.search.enmus.IndexTargetMethodEnum;
import com.legendshop.search.enmus.IndexTypeEnum;
import com.legendshop.search.service.SearchProductIndexService;
import com.legendshop.search.service.strategy.IndexServiceContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 商品索引的控制层
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/search/index", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "商品索引")
@RequiredArgsConstructor
public class AdminSearchIndexController {

	private final SearchProductIndexService searchProductIndexService;
	private final StringRedisTemplate redisTemplate;
	private final IndexServiceContext indexServiceContext;

	/**
	 * 重建索引
	 *
	 * @param type {@link IndexTypeEnum}
	 * @return
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin_search_index_updateAll')")
	@Operation(summary = "【后台】重建所有商品索引 0全部 1商品 2店铺 3营销 4优惠券 ", description = "")
	public R<String> reIndex(Integer type) {
		Boolean exist = IndexTypeEnum.isExist(type);
		if (!exist) {
			return R.fail("重建失败，无所识别索引类型");
		}
		log.info("开始重建所有索引， type = {}", type);

		// 设置key以保持字符串值，如果key不存在，则设置过期超时。
		Boolean flag = redisTemplate.opsForValue().setIfAbsent(CacheConstants.INDEX_INIT, type.toString(), 3, TimeUnit.MINUTES);
		IndexTypeEnum integralDealTypeEnum = EnumUtil.likeValueOf(IndexTypeEnum.class, type);
		if (!flag) {
			return R.fail("正在重建" + integralDealTypeEnum.name() + "索引中, 暂时无法重建索引");
		}
		indexServiceContext.createReIndexLog(integralDealTypeEnum.name(), IndexTargetMethodEnum.CREATE.getValue(), null, null);
		return R.ok("正在重建索引, 请耐心等待");
	}

	@PostMapping(value = "/reIndexCount/count")
	@Operation(summary = "【后台】统计es索引和数据库索引 ", description = "")
	public R<IndexCountDTO> reIndexCount() throws IOException {
		log.info("reIndexCount starting ......");
		return R.ok(searchProductIndexService.reIndexCount());
	}

	/**
	 * 根据商品id重建索引
	 *
	 * @return
	 */
	@PutMapping(value = "/{productId}")
	@PreAuthorize("@pms.hasPermission('admin_search_index_update')")
	@Operation(summary = "【后台】根据商品id重建索引", description = "")
	@Parameter(name = "productId", description = "商品id", required = true)
	public R<String> reIndex(@PathVariable("productId") Long productId) {
		String builtResult = "";
		searchProductIndexService.delByProductIdIndex(productId);
		searchProductIndexService.initByProductIdIndex(productId);
		return R.fail("重建" + productId + "商品索引失败");
	}

}
