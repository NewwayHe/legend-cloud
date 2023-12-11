/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志表(OperatorLog)DTO
 *
 * @author legendshop
 * @since 2023-08-29 14:20:23
 */
@Data
@ApiModel(value = "操作日志表DTO")
public class OperatorLogDTO implements Serializable {

	private static final long serialVersionUID = -26839493845603017L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Long userId;

	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;

	/**
	 * 用户类型：UserTypeEnum
	 */
	@ApiModelProperty(value = "用户类型：UserTypeEnum")
	private String userType;

	/**
	 * 事件类型：EventTypeEnum
	 */
	@ApiModelProperty(value = "事件类型：EventTypeEnum")
	private String eventType;

	/**
	 * 修改前
	 */
	@ApiModelProperty(value = "修改前")
	private String beforeModification;

	/**
	 * 修改后
	 */
	@ApiModelProperty(value = "修改后")
	private String afterModification;

	/**
	 * 修改前
	 */
	@ApiModelProperty(value = "修改前")
	private Object beforeModificationObject;

	/**
	 * 修改后
	 */
	@ApiModelProperty(value = "修改后")
	private Object afterModificationObject;

	/**
	 * 操作人ID
	 */
	@ApiModelProperty(value = "操作人ID")
	private Long operatorId;

	/**
	 * 操作人名称
	 */
	@ApiModelProperty(value = "操作人名称")
	private String operatorName;

	/**
	 * 操作人类型：UserTypeEnum
	 */
	@ApiModelProperty(value = "操作人类型：UserTypeEnum")
	private String operatorType;

	/**
	 * 所属IP
	 */
	@ApiModelProperty(value = "所属IP")
	private String remoteIp;

	/**
	 * 请求来源：VisitSourceEnum
	 */
	@ApiModelProperty(value = "请求来源：VisitSourceEnum")
	private String source;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

}
