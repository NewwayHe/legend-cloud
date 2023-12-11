/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 业务配置DTO
 *
 * @author legendshop
 */
@Data
public class BusinessSettingDTO implements Serializable {


	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 配置类型
	 * {@link com.legendshop.basic.enums.BusinessSettingTypeEnum}
	 */
	private String type;


	/**
	 * 常量值
	 */
	private String value;


}
