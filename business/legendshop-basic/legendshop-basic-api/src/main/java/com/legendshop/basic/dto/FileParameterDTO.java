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
 * 文件模板表(FileParam)实体类
 *
 * @author legendshop
 */
@Data
public class FileParameterDTO implements Serializable {

	private static final long serialVersionUID = 4798353975078208020L;

	/**
	 * 文件名称
	 */
	private String name;


	/**
	 * 文件内容
	 */
	private String value;


	/**
	 * 文件说明
	 */
	private String memo;


	/**
	 * 是否可以线上修改
	 */
	private String changeOnline;

}
