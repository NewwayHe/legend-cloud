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
import com.legendshop.basic.enums.ReceiverBusinessTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息已读类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_receiver")
public class Receiver implements Serializable, GenericEntity<Long> {


	private static final long serialVersionUID = -4100649236832530250L;
	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "RECEIVER_SEQ")
	private Long id;


	/**
	 * 业务ID
	 */
	@Column(name = "business_id")
	private Long businessId;

	/**
	 * 业务类型
	 * {@link ReceiverBusinessTypeEnum}
	 */
	@Column(name = "business_type")
	private Integer businessType;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户类型  MsgReceiverTypeEnum
	 */
	@Column(name = "user_type")
	private Integer userType;

	/**
	 * 状态 0未读 1已读 只插入已读数据
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Date updateTime;


}
