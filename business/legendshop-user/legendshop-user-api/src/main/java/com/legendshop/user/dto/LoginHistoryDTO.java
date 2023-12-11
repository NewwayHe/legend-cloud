/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录历史表(LoginHist)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "登陆历史")
public class LoginHistoryDTO implements Serializable {


	private static final long serialVersionUID = -6599703667689541443L;

	/**
	 * ID
	 */
	@Schema(description = "id")
	private Long id;


	/**
	 * 用户Id
	 */
	@Schema(description = "用户Id")
	private Long userId;


	/**
	 * 地区
	 */
	@Schema(description = "地区")
	private String area;


	/**
	 * 国家
	 */
	@Schema(description = "国家")
	private String country;


	/**
	 * IP
	 */
	@Schema(description = "IP")
	private String ip;


	/**
	 * 时间
	 */
	@Schema(description = "时间")
	private Date time;


	/**
	 * 登录类型：USER,SELLER_TYPE,见LoginUserTypeEnum
	 */
	@Schema(description = "登录类型")
	private String loginType;


	/**
	 * 登录的来源,PC，MOBILE,APP, 见VisitSourceEnum
	 */
	@Schema(description = "登录的来源")
	private String loginSource;

	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickName;

}
