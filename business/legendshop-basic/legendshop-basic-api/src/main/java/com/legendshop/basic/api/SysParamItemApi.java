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
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 系统主配置项服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "sysParamItemApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface SysParamItemApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据父级ID获取系统参数项列表
	 *
	 * @param parentId 父级ID
	 * @return 系统参数项列表
	 */
	@GetMapping(PREFIX + "/sysParamItem/{parentId}")
	R<List<SysParamItemDTO>> getListByParentId(@PathVariable("parentId") Long parentId);

	/**
	 * 修改配置项的值
	 *
	 * @param batchUpdateSysParamDTO 批量更新系统参数DTO
	 * @return 更新操作的结果
	 */
	@PutMapping(PREFIX + "/sysParamItem/value")
	R<Void> updateValueOnlyById(@RequestBody BatchUpdateSysParamDTO batchUpdateSysParamDTO);


	/**
	 * 根据ID获得配置项的值
	 *
	 * @param id
	 * @return
	 */
	@Deprecated
	@GetMapping(PREFIX + "/sysParamItem/sysParamItemById/{id}")
	R<SysParamItemDTO> getSysParamId(@PathVariable("id") Long id);
}
