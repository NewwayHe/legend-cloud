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

import java.io.Serializable;
import java.util.Date;

/**
 * (Protocol)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_protocol")
public class Protocol implements Serializable, GenericEntity<Long> {

	private static final long serialVersionUID = -74109048906955308L;


	/**
	 * 主键ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROTOCOL_SEQ")
	private Long id;


	/**
	 * 协议代号
	 */
	@Column(name = "code")
	private String code;


	/**
	 * 协议名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 协议链接
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 协议富文本
	 */
	@Column(name = "text")
	private String text;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;
	/**
	 * 协议类型
	 * 0 协议链接
	 * 1 协议富文本
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
