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
 * 敏感字过滤表(SensWord)DTO
 *
 * @author legendshop
 * @since 2021-06-30 14:19:31
 */
@Data
@Schema(description = "敏感字过滤表DTO")
public class SensWordDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -21303821834707593L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 关键字
	 */
	@Schema(description = "关键字")
	private String words;

	/**
	 * 是否全局敏感字
	 */
	@Schema(description = "是否全局敏感字")
	private Integer isGlobal;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private String creator;


}
