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
import java.util.Date;

/**
 * 下载历史(DolLog)实体类
 *
 * @author legendshop
 */
@Data
public class DolLogDTO implements Serializable {


	private static final long serialVersionUID = -2288644218903136312L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 商城名称
	 */
	private String shopName;


	/**
	 * 地区
	 */
	private String area;


	/**
	 * 国家
	 */
	private String country;


	/**
	 * ip
	 */
	private String ip;


	/**
	 * 文件名称
	 */
	private String fileName;


	/**
	 * 下载时间
	 */
	private Date recDate;

}
