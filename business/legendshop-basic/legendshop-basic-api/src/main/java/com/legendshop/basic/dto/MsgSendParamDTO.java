/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.MsgSendParamEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 模板参数替换内容
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgSendParamDTO implements Serializable {

	/**
	 * 模板配置的属性名
	 */
	private MsgSendParamEnum msgSendParam;

	/**
	 * 替换的实际值
	 */
	private String value;

	/**
	 * 颜色
	 */
	private String color;
}
