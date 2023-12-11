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
import com.legendshop.basic.enums.MsgReceiverTypeEnum;
import lombok.Data;

/**
 * 消息状态(MsgStatus)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_msg_receiver")
public class MsgReceiver implements GenericEntity<Long> {

	private static final long serialVersionUID = 812895883069163453L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "MSG_STATUS_SEQ")
	private Long id;


	/**
	 * 消息ID
	 */
	@Column(name = "msg_id")
	private Long msgId;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * {@link MsgReceiverTypeEnum}
	 */
	@Column(name = "type")
	private Integer type;

}
