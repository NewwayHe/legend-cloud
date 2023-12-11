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

/**
 * @author legendshop
 */
@Schema(description = "传递到后台的文件")
public interface ServiceMultipartFile {

	@Schema(description = "文件名字")
	String getName();

	@Schema(description = "文件原名字")
	String getOriginalFilename();

	@Schema(description = "是否为空")
	boolean isEmpty();

	@Schema(description = "文件大小")
	long getSize();

	@Schema(description = "文件路径")
	String getPath();

	@Schema(description = "获取附件Id")
	Long getAttachmentId();

	void setAttachmentId(Long attachmentId);
}
