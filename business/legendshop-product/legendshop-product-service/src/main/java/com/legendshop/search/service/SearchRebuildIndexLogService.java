/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.search.dto.SearchRebuildIndexLogDTO;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)表服务接口
 *
 * @author legendshop
 * @since 2022-02-18 10:50:56
 */
public interface SearchRebuildIndexLogService {

	/**
	 * 通过 索引ID进行查找
	 *
	 * @param id
	 * @return
	 */
	SearchRebuildIndexLogDTO getById(Long id);


	/**
	 * 保存索引重建日志
	 *
	 * @param searchRebuildIndexLogDTO
	 * @return
	 */
	R<Long> save(SearchRebuildIndexLogDTO searchRebuildIndexLogDTO);

	/**
	 * 修改索引重建状态
	 *
	 * @param searchRebuildIndexLogDTO
	 * @return
	 */
	R updateStatus(SearchRebuildIndexLogDTO searchRebuildIndexLogDTO);

}
