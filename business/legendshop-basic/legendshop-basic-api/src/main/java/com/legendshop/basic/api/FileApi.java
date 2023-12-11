/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件管理
 *
 * @author legendshop
 */
@FeignClient(contextId = "fileApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface FileApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 使用示例
	 * Response download = this.fileClient.download(param, FileScopeEnum.PRIVATE.name());
	 * Response.Body body = download.body();
	 * body.asInputStream();
	 */
	@GetMapping(value = PREFIX + "/file/download")
	Response download(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope);


	@PostMapping(value = PREFIX + "/file/createExportExcelTask")
	R<String> createExportExcelTask(@RequestBody ExportExcelTaskDTO exportExcelTaskDTO);
}
