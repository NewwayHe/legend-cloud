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
 * 后台统计表(AdminDashboard)实体类
 *
 * @author legendshop
 */
@Data
public class AdminDashboardDTO implements Serializable {


	private static final long serialVersionUID = -2314153508709488798L;
	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 名字
	 */
	private String dashboardName;


	/**
	 * 参考值
	 */
	private String value;


	/**
	 * 记录时间
	 */
	private Date recDate;

}
