/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * @author legendshop
 */
public enum ReceiverBusinessTypeEnum implements IntegerEnum {

	/**
	 * 公告
	 */
	NOTICE(1),

	/**
	 * 系统通知
	 */
	SYSTEM_MESSAGE(2),

	/**
	 * 点赞
	 */
	THUMB(3),

	/**
	 * 收藏
	 */
	COLLECT(4),

	/**
	 * 文章评论
	 */
	ARTICLE_COMMENT(5),

	/**
	 * 发现文章评论
	 */
	DISCOVER_ARTICLE_COMMENT(6),

	;


	private Integer value;

	@Override
	public Integer value() {
		return value;
	}


	ReceiverBusinessTypeEnum(Integer value) {
		this.value = value;
	}
}
