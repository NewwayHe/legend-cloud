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

/**
 * 文件模板表(FileParam)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_file_param")
public class FileParameter implements GenericEntity<String> {

	private static final long serialVersionUID = 111589790255423168L;

	/**
	 * 文件名称
	 */
	@Id
	@Column(name = "name")
	@GeneratedValue(strategy = GenerationType.ASSIGNED)
	private String name;


	/**
	 * 文件内容
	 */
	@Column(name = "value")
	private String value;


	/**
	 * 文件说明
	 */
	@Column(name = "memo")
	private String memo;


	/**
	 * 是否可以线上修改
	 */
	@Column(name = "change_online")
	private String changeOnline;


	@Override
	@Transient
	public String getId() {
		return name;
	}

	@Override
	public void setId(String id) {
		this.name = id;

	}

}
