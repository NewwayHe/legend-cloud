/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户安全配置(UserSecurity)实体类
 *
 * @author legendshop
 */
@Data
public class UserSecurityDTO implements Serializable {

	private static final long serialVersionUID = -8432145974877246651L;

	/**
	 * ID
	 */
	private Long id;


	/**
	 * 用户id
	 */
	private Long userId;


	/**
	 * 安全级别
	 */
	private Integer secLevel;


	/**
	 * 是否验证邮箱
	 */
	private Integer mailVerifn;


	/**
	 * 是否验证手机
	 */
	private Integer phoneVerifn;


	/**
	 * 是否验证支付密码
	 */
	private Integer paypassVerifn;


	/**
	 * 短信发送时间
	 */
	private Date sendSmsTime;


	/**
	 * 邮件发送时间
	 */
	private Date sendMailTime;


	/**
	 * 建立时间
	 */
	private Date createTime;


	/**
	 * 短信验证码
	 */
	private String validateCode;


	/**
	 * 邮箱验证码
	 */
	private String emailCode;


	/**
	 * 支付密码强度
	 */
	private Integer payStrength;


	/**
	 * 将要更新到这个邮箱
	 */
	private String updateMail;


	/**
	 * 验证次数
	 */
	private Integer times;

}
