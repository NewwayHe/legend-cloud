/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 群发消息状态(PubReceiver)实体类
 *
 * @author legendshop
 * @since 2021-06-16 11:23:49
 */
@Data
@Entity
@Table(name = "ls_pub_receiver")
public class PubReceiver implements GenericEntity<Long> {

	private static final long serialVersionUID = -77011642223534118L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "pubReceiver_SEQ")
	private Long id;

	/**
	 * 公告ID
	 */
	@Column(name = "pub_id")
	private Long pubId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 状态，0未读，1已读
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 1：普通用户 2：商家
	 */
	@Column(name = "type")
	private Integer type;

}
