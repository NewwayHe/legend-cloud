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
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import com.legendshop.shop.service.IndustryDirectoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表控制层
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class IndustryDirectoryApiImpl implements IndustryDirectoryApi {
	/**
	 * 行业目录(IndustryDirectory)服务对象
	 */
	private final IndustryDirectoryService industryDirectoryService;

	/**
	 * 获取分页列表
	 */
	@Override
	public R<IndustryDirectoryDTO> getById(@RequestParam("id") Long id) {
		return R.ok(this.industryDirectoryService.getById(id));
	}

	@Override
	public R<List<IndustryDirectoryDTO>> queryById(@RequestBody List<Long> ids) {
		return R.ok(this.industryDirectoryService.queryById(ids));
	}


}
