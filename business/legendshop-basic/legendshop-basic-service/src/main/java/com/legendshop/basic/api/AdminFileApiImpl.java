/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.dto.AttachmentFileFolderDTO;
import com.legendshop.basic.service.AttachmentFileFolderService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class AdminFileApiImpl implements AdminFileApi {

	private final AttachmentFileFolderService attachmentFileFolderService;

	/**
	 * 初始化文件夹根目录
	 *
	 * @return
	 */
	@Override
	public R<Long> checkFile() {
		Long adminByParentId = attachmentFileFolderService.getAdminByParentId(-1L);
		if (ObjectUtil.isNull(adminByParentId)) {
			AttachmentFileFolderDTO attachmentFileFolderDTO = new AttachmentFileFolderDTO();
			attachmentFileFolderDTO.setParentId(-1L);
			attachmentFileFolderDTO.setTypeId(0);
			attachmentFileFolderDTO.setUserType(0);
			attachmentFileFolderDTO.setFileName("平台根目录");
			attachmentFileFolderDTO.setId(1L);
			attachmentFileFolderDTO.setCreateTime(new Date());
			attachmentFileFolderService.save(attachmentFileFolderDTO);

			adminByParentId = attachmentFileFolderService.getAdminByParentId(-1L);
		}
		return R.ok(adminByParentId);
	}
}
