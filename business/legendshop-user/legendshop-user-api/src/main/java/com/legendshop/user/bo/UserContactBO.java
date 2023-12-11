/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户提货信息
 *
 * @author legendshop
 */
@ApiModel(value = "用户提货信息")
@Data
public class UserContactBO implements Serializable {

	private static final long serialVersionUID = -28716747785460097L;


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
	 * 接受人名称
	 */
	@ApiModelProperty(value = "收货人名称")
	private String receiver;


	/**
	 * 手机
	 */
	@ApiModelProperty(value = "手机")
	private String mobile;


	/**
	 * 是否默认
	 */
	@ApiModelProperty(value = "是否默认")
	private Boolean defaultFlag;


	/**
	 * 建立时间
	 */
	@ApiModelProperty(value = "建立时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;


	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
}
