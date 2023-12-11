/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "")
public class AdminProtocolDTO implements Serializable {

	private static final long serialVersionUID = 2825256785304210086L;

	@Schema(description = "id")
	private Long id;

	/**
	 * 协议url
	 */

	@Schema(description = "协议url")
	private String url;
	/**
	 * 协议富文本
	 */

	@Schema(description = "协议富文本")
	private String text;
	/**
	 * 协议类型
	 * 0 协议连接
	 * 1 协议富文本
	 */

	@Schema(description = "协议类型 0 协议连接 1协议富文本")
	private Integer type;

}
