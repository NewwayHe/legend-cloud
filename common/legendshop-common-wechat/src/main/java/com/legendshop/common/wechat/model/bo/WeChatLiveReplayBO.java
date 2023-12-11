/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.wechat.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 直播回放
 *
 * @author legendshop
 */
@Data
public class WeChatLiveReplayBO implements Serializable {

	private static final long serialVersionUID = 8118850730120612176L;

	private Integer id;

	/**
	 * 过期时间
	 */
	private String expireTime;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 直播回放地址
	 */
	private String mediaUrl;
}
