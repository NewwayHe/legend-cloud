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

import java.util.Date;

/**
 * 用户反馈表(UserFeedback)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_user_feedback")
public class UserFeedBack implements GenericEntity<Long> {

	private static final long serialVersionUID = 499250000953198667L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_FEEDBACK_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 反馈人名字
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 联系方式
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 发起反馈的IP地址
	 */
	@Column(name = "ip")
	private String ip;


	/**
	 * 反馈内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 记录时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 是否已经回复
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 回复的管理员ID
	 */
	@Column(name = "resp_mgnt_id")
	private Long respMgntId;


	/**
	 * 处理意见
	 */
	@Column(name = "resp_content")
	private String respContent;


	/**
	 * 处理时间
	 */
	@Column(name = "resp_date")
	private Date respDate;


	/**
	 * 反馈来源(1:PC 2:android 3:wap 4:IOS)
	 */
	@Column(name = "feedback_source")
	private String feedbackSource;

}
