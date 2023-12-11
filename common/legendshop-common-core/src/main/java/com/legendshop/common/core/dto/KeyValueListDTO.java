/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 键值对DTO, key类型为String, value类型为List<String>.
 *
 * @author legendshop
 */
@Data
public class KeyValueListDTO implements Serializable, Cloneable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 5568358970483740841L;

	/**
	 * The key.
	 */
	private String key;

	/**
	 * The value.
	 */
	private List<String> valueList;


}
