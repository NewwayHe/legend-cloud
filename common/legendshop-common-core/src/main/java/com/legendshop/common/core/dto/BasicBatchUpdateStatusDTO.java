/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于批量更新状态的类
 *
 * @author legendshop
 */
@Data
@ApiModel(value = "用于批量更新状态的类")
public class BasicBatchUpdateStatusDTO implements Serializable {


	@NotEmpty(message = "处理的id集合不能为空")
	@ApiModelProperty(value = "id集合")
	private List<Long> ids;


	@NotNull(message = "状态不能为空")
	@ApiModelProperty(value = "状态")
	private Integer status;
}
