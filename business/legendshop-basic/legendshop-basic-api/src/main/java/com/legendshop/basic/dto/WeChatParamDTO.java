/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class WeChatParamDTO {

	/**
	 * openid
	 */
	private List<String> touser;

	/**
	 * 模板id
	 */
	private String templateId;

	/**
	 * 模板替换的内容
	 */
	private List<MsgSendParamDTO> dataDTOList;

	/**
	 * 跳转路径替换参数
	 */
	private List<MsgSendParamDTO> urlParamList;

	/**
	 * 跳转路径
	 */
	private String url;

}
