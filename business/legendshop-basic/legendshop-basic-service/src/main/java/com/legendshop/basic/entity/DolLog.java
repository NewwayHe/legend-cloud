/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * 下载历史(DolLog)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_dol_log")
public class DolLog implements GenericEntity<Long> {

	private static final long serialVersionUID = 708032040424014335L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "DOG_LOG_SEQ")
	private Long id;


	/**
	 * 商城名称
	 */
	@Column(name = "shop_name")
	private String shopName;


	/**
	 * 地区
	 */
	@Column(name = "area")
	private String area;


	/**
	 * 国家
	 */
	@Column(name = "country")
	private String country;


	/**
	 * ip
	 */
	@Column(name = "ip")
	private String ip;


	/**
	 * 文件名称
	 */
	@Column(name = "file_name")
	private String fileName;


	/**
	 * 下载时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

}
