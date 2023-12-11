/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 图片空间批量修改文件路径
 *
 * @author legendshop
 */
@Data
@Schema(description = "图片空间批量修改文件路径DTO")
public class

FileFolderUpdateDTO {

	@Schema(description = "移动文件列表")
	List<AttachmentFileFolderDTO> attachmentFileFolderDTOList;

	@Schema(description = "目标文件夹id")
	Long fileFolderId;
}
