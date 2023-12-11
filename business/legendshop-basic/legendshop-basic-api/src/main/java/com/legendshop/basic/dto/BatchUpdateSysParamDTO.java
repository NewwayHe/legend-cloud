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
 * @author legendshop
 */
@Data
public class BatchUpdateSysParamDTO {

	@Schema(description = "修改名称")
	private String name;

	@Schema(description = "分组")
	private String groupBy;

	@Schema(description = "参数")
	private List<SysParamValueItemDTO> sysParamValueItemDTOS;

}
