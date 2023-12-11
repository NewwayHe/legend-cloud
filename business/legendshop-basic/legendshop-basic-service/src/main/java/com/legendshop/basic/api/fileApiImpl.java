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
import com.legendshop.basic.service.AttachmentService;
import com.legendshop.basic.service.ExportExcelTaskService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.common.core.expetion.BusinessException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@Slf4j
public class fileApiImpl {

	static final String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;
	private final AttachmentService attachmentService;
	private final ExportExcelTaskService exportExcelTaskService;

	/**
	 * 文件下载，只能通过Feign内部调用。
	 */
	@GetMapping(value = PREFIX + "/file/download")
	public void download(@RequestParam(value = "fileName") String fileName, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, HttpServletResponse servletResponse) {
		log.info("文件下载，地址：{},Scope:{}", fileName, scope);
		try (InputStream inputStream = this.attachmentService.download(fileName, scope);
			 OutputStream outputStream = servletResponse.getOutputStream()
		) {
			int length;
			byte[] temp = new byte[1024 * 10];
			while ((length = inputStream.read(temp)) != -1) {
				outputStream.write(temp, 0, length);
			}
			outputStream.flush();
		} catch (IOException e) {
			log.error("", e);
			throw new BusinessException("加载返回文件流异常");
		}
	}

	@PostMapping(value = PREFIX + "/file/createExportExcelTask")
	public R<String> createExportExcelTask(@RequestBody ExportExcelTaskDTO exportExcelTaskDTO) {
		//3、返回Task记录文件名
		return exportExcelTaskService.save(exportExcelTaskDTO);
	}


}
