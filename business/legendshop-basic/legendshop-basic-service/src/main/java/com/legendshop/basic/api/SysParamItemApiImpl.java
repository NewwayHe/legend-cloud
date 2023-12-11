/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.BatchUpdateSysParamDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.service.SysParamItemService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class SysParamItemApiImpl implements SysParamItemApi {
	final SysParamItemService sysParamItemService;
	final SysParamsService sysParamsService;

	@Override
	public R<List<SysParamItemDTO>> getListByParentId(@PathVariable("parentId") Long parentId) {
		return R.ok(sysParamItemService.getListByParentId(parentId));
	}


	@Override
	public R<Void> updateValueOnlyById(@RequestBody BatchUpdateSysParamDTO batchUpdateSysParamDTO) {
		sysParamsService.updateValueOnlyById(batchUpdateSysParamDTO);
		return R.ok();
	}

	@Override
	public R<SysParamItemDTO> getSysParamId(@PathVariable("id") Long id) {
		return sysParamItemService.getSysParamId(id);
	}

}
