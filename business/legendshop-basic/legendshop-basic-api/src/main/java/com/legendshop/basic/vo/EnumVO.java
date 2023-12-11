/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class EnumVO implements Serializable {
	private static final long serialVersionUID = -9052107897225964523L;

	@Schema(description = "枚举名称")
	private String name;

	@Schema(description = "编码")
	private Integer code;

	@Schema(description = "描述")
	private String desc;

	public EnumVO(String name, Integer code, String desc) {
		this.name = name;
		this.code = code;
		this.desc = desc;
	}

	public EnumVO() {
	}
}
