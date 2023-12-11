/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import com.legendshop.basic.enums.UserOperationLogSideEnum;
import com.legendshop.basic.enums.UserOperationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "操作日志")
public class UserOperationLogDTO implements Serializable {

	private static final long serialVersionUID = 2924207690445320445L;
	private Long id;

	/**
	 * 用户Id不能为空，默认为0
	 */
	@Schema(description = "用户手机号，默认为0")
	private String userMobile;

	@Schema(description = "用户名称")
	private String userName;

	/**
	 * 操作名称
	 */
	@Schema(description = "操作名称")
	private String name;

	/**
	 * 操作编码
	 */
	@Schema(description = "操作编码")
	private String code;

	/**
	 * {@link UserOperationTypeEnum}
	 * 操作类型
	 */
	@Schema(description = "操作类型")
	private String type;

	/**
	 * 主要请求地址
	 */
	@Schema(description = "主要请求地址")
	private String url;

	/**
	 * 发起时间
	 */
	@Schema(description = "发起时间")
	private Date time;

	/**
	 * 操作系统
	 */
	@Schema(description = "操作系统")
	private String os;

	/**
	 * 浏览器名称
	 */
	@Schema(description = "浏览器名称")
	private String browserName;

	/**
	 * 浏览器版本
	 */
	@Schema(description = "浏览器版本")
	private String browserVersion;

	/**
	 * {@link UserOperationLogSideEnum}
	 */
	@Schema(description = "操作端")
	private String side;
}
