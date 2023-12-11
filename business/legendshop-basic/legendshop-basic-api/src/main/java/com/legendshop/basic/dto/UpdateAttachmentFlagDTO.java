/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.UpdateFilePathEnum;
import com.legendshop.common.core.annotation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Schema(description = "修改附件图片空间状态")
@Data
public class UpdateAttachmentFlagDTO {
	private List<String> filePath;

	@Schema(description = "1第一个tab上传图片,2第二个tab上传视频,3,zip上传")
	@EnumValid(target = UpdateFilePathEnum.class, message = "上传类型不匹配")
	private Long tabType;

	@Schema(description = "文件名")
	private String fileName;

	@Schema(description = "id")
	private Long userId;
}
