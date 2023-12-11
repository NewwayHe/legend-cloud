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
import org.springframework.web.multipart.MultipartFile;

/**
 * @author legendshop
 */
@Schema(description = "图片空间,上传图片")
@Data
public class UpLoadFileNewDTO {

	@Schema(description = "上传用户")
	private Long userId;

	@Schema(description = "上传文件内容")
	private MultipartFile file;

	@Schema(description = "文件来源")
	private String fileSource;

	@Schema(description = "文件夹id")
	private Long fileFolderId;

	private String scope;

	@Schema(description = "商家id")
	private Long shopId;

	@Schema(description = "是否是图片")
	private Boolean uploadType;

	@Schema(description = "数据库图片地址")
	private String picPath;

	@Schema(description = "短链接")
	private String shortPath;
}
