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
 * for UserDetail
 *
 * @author legendshop
 */
@Data
public class UserFormDTO implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -4363234793127853507L;

	/**
	 * The user id.
	 */
	private String userId;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The password.
	 */
	private String password;

	/**
	 * The password old.
	 */
	private String passwordOld;

	// 默认注册用户是可以使用，0为不可使用
	/**
	 * The enabled.
	 */
	private String enabled = "1";

	/**
	 * The note.
	 */
	private String note;

	/**
	 * The grade id.
	 */
	private Integer gradeId;

	/**
	 * The user name.
	 */
	private String userName;

	/**
	 * The nick name.
	 */
	private String nickName;

	/**
	 * The user mail.
	 */
	private String userMail;

	/**
	 * The user adds.
	 */
	private String userAdds;

	/**
	 * The user tel.
	 */
	private String userTel;

	/**
	 * The user postcode.
	 */
	private String userPostcode;

	/**
	 * The msn.
	 */
	private String msn;

	/**
	 * The qq.
	 */
	private String qq;

	/**
	 * The fax.
	 */
	private String fax;

	/**
	 * The modify time.
	 */
	private Date modifyTime;

	/**
	 * The user regtime.
	 */
	private Date userRegtime;

	/**
	 * The user regip.
	 */
	private String userRegip;

	/**
	 * The user lasttime.
	 */
	private Date userLasttime;

	/**
	 * The user lastip.
	 */
	private String userLastip;

	/**
	 * The user memo.
	 */
	private String userMemo;

	/**
	 * The sex.
	 */
	private String sex;

	/**
	 * The birth date.
	 */
	private Date birthDate;

	/**
	 * The user mobile.
	 */
	private String userMobile;
	// 生日的年月日
	/**
	 * The user birth year.
	 */
	private String userBirthYear;

	/**
	 * The user birth month.
	 */
	private String userBirthMonth;

	/**
	 * The user birth day.
	 */
	private String userBirthDay;

	/**
	 * 图片验证码
	 **/
	private String randNum;

}
