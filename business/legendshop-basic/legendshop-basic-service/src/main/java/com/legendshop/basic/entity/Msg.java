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
import com.legendshop.basic.enums.MsgSendTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * 站内信(Msg)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_msg")
public class Msg implements GenericEntity<Long> {

	private static final long serialVersionUID = -69425171949694366L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "MSG_SEQ")
	private Long id;


	/**
	 * 发送人用户id
	 */
	@Column(name = "send_user_id")
	private Long sendUserId;

	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;

	/**
	 * 内容
	 */

	@Column(name = "content")
	private String content;

	/**
	 * 是否全局消息
	 */
	@Column(name = "global_flag")
	private Boolean globalFlag;


	/**
	 * 删除状态
	 */
	@Column(name = "delete_status")
	private Integer deleteStatus;


	/**
	 * 类型 {@link MsgSendTypeEnum}
	 */
	@Column(name = "type")
	private Integer type;

	/**
	 * 详情id
	 */
	@Column(name = "detail_id")
	private Long detailId;

	/**
	 * 记录时间
	 */
	@Column(name = "rec_date")
	private Date recDate;

}
