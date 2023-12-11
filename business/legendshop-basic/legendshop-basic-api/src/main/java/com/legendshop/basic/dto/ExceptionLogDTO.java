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
import lombok.Data;

import java.io.Serializable;

/**
 * (ExceptionLog)DTO
 *
 * @author legendshop
 * @since 2020-09-25 10:20:22
 */
@Data
public class ExceptionLogDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 373414019894325005L;
	/**
	 * 记录类型
	 */
	private String type;

	/**
	 * 等级
	 */
	private Integer level;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 关键字
	 */
	private String keyword;

	/**
	 * 描述
	 */
	private String description;

}
