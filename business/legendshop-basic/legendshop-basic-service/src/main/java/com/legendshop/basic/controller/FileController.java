/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.hutool.core.util.StrUtil;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.dto.AttachmentFileHistoryDTO;
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.enums.FileSourceEnum;
import com.legendshop.basic.service.AttachmentFileFolderService;
import com.legendshop.basic.service.AttachmentService;
import com.legendshop.basic.service.ExportExcelTaskService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 图片处理
 *
 * @author legendshop
 */
@Tag(name = "图片上传")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FileController {

	private final AttachmentService attachmentService;
	private final ExportExcelTaskService exportExcelTaskService;
	private final AttachmentFileFolderService attachmentFileFolderService;

	/**
	 * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
	 *
	 * @param file     图片
	 * @param fileType 0:图片 1:视频
	 */
	@PostMapping("/upload")
	@Operation(summary = "【公共】文件上传")
	public R<Map<String, String>> upload(@RequestParam("file") MultipartFile file, @RequestParam String fileSource, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, @RequestParam Long fileType) throws IOException {
		if (!FileSourceEnum.contains(fileSource)) {
			log.warn("图片上传失败，fileSource为空");
			return R.fail("图片上传失败");
		}

		return this.attachmentService.uploadFile(SecurityUtils.getUserId(), file, fileSource, scope, fileType);
	}

	@PostMapping("/uploadZip")
	@Operation(summary = "【公共】文件上传")
	public R<Map<String, String>> uploadZip(@RequestParam("file") MultipartFile file, @RequestParam String fileSource, @RequestParam(value = "scope", defaultValue = "PUBLIC") String scope, @RequestParam Long fileType) throws IOException {
		if (!FileSourceEnum.contains(fileSource)) {
			log.warn("图片上传失败，fileSource为空");
			return R.fail("图片上传失败");
		}

		return this.attachmentService.uploadFileZip(SecurityUtils.getUserId(), file, fileSource, scope, fileType);
	}


	@PostMapping(value = "/saveHistory")
	@Operation(summary = "【公共】保存商品历史路径")
	public R<List<List<AttachmentFileFolderDTO>>> saveHistory(@RequestBody List<Long> id) {
		return attachmentFileFolderService.saveHistory(SecurityUtils.getUserId(), SecurityUtils.getUserType(), id);
	}


	@PostMapping(value = "/saveHistoryNew")
	@Operation(summary = "【公共】保存商品历史路径")
	public R<List<List<AttachmentFileFolderDTO>>> saveHistoryNew(@RequestBody AttachmentFileHistoryDTO attachmentFileHistoryDTO) {
		String userType = SecurityUtils.getUserType();
		if (StrUtil.isBlank(attachmentFileHistoryDTO.getUserType())) {
			attachmentFileHistoryDTO.setUserType(userType);
		}
		return attachmentFileFolderService.saveHistory(SecurityUtils.getUserId(), attachmentFileHistoryDTO.getUserType(), attachmentFileHistoryDTO.getFolderIdList());
	}

	@PostMapping(value = "/createExportExcelTask")
	@Operation(summary = "【公共】生成Excel导出任务")
	public R<String> createExportExcelTask(@RequestBody ExportExcelTaskDTO exportExcelTaskDTO) {
		//3、返回Task记录文件名
		return exportExcelTaskService.save(exportExcelTaskDTO);
	}
}
