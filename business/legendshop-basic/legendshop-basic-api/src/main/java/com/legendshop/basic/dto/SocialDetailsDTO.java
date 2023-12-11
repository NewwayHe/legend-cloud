/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

/**
 * 社交登录的配置表
 *
 * @author legendshop
 */
@Data
public class SocialDetailsDTO extends BaseDTO implements GenericEntity<Long> {

	private static final long serialVersionUID = -94255560368226353L;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * app_id
	 */
	private String appId;


	/**
	 * app_secret
	 */
	private String appSecret;


	/**
	 * 回调url
	 */
	private String redirectUrl;

	/**
	 * 是否启用
	 */
	private Boolean enableFlag;

}
