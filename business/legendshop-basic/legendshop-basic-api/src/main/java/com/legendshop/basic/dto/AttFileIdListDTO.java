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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片空间批量删除Id列表
 *
 * @author legendshop
 */
@Data
public class AttFileIdListDTO implements Serializable {

	private static final long serialVersionUID = -5787381639992647971L;

	@NotNull(message = "文件ID不能为空")
	@Schema(description = "需要修改的文件id")
	private Long id;

	@NotNull(message = "文件类型不能为空")
	@Schema(description = "文件类型 1:文件夹, 0:附件")
	private Integer type;

	@Schema
	private Long shopId;
}
