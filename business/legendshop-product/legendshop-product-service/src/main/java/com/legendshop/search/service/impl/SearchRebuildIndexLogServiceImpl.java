/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.search.dao.SearchRebuildIndexLogDao;
import com.legendshop.search.dto.SearchRebuildIndexLogDTO;
import com.legendshop.search.service.SearchRebuildIndexLogService;
import com.legendshop.search.service.converter.SearchRebuildIndexLogConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)表服务实现类
 *
 * @author legendshop
 * @since 2022-02-18 10:50:57
 */
@Service
@Slf4j
@AllArgsConstructor
public class SearchRebuildIndexLogServiceImpl implements SearchRebuildIndexLogService {

	private final SearchRebuildIndexLogConverter converter;
	private final SearchRebuildIndexLogDao searchRebuildIndexLogDao;

	@Override
	public SearchRebuildIndexLogDTO getById(Long id) {
		return converter.to(searchRebuildIndexLogDao.getById(id));
	}

	@Override
	public R<Long> save(SearchRebuildIndexLogDTO searchRebuildIndexLogDTO) {
		Long result = searchRebuildIndexLogDao.save(converter.from(searchRebuildIndexLogDTO));
		if (result > 0) {
			return R.ok(result);
		} else {
			return R.fail();
		}
	}

	@Override
	public R updateStatus(SearchRebuildIndexLogDTO searchRebuildIndexLogDTO) {
		int result = searchRebuildIndexLogDao.updateState(searchRebuildIndexLogDTO.getId(), searchRebuildIndexLogDTO.getStatus(), searchRebuildIndexLogDTO.getRemark());
		if (result >= 0) {
			return R.ok();
		} else {
			return R.fail();
		}
	}
}
