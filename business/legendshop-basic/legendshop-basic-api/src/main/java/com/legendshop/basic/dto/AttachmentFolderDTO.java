/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * (AttachmentFolder)DTO
 *
 * @author legendshop
 * @since 2021-07-06 17:35:59
 */
@Data
@Schema(description = "DTO")
public class AttachmentFolderDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 180641527992893366L;

	/**
	 * 附件管理id
	 */
	@Schema(description = "附件管理id")
	private Long attachmentId;

	/**
	 * 附件管理文件夹id
	 */
	@Schema(description = "附件管理文件夹id")
	private Long fileFolderId;

	/**
	 * 附件路径
	 */
	@Schema(description = "附件路径")
	private String attachmentFolder;


}
