/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 促销活动状态
 *
 * @author legendshop
 */
public enum PromotionStatusEnum implements IntegerEnum {

	/**
	 * 未发布
	 **/
	NO_PUBLISH(0),

	/**
	 * 进行中
	 **/
	ONLINE(1),

	/**
	 * 已暂停
	 **/
	PAUSE(2),

	/**
	 * 已失效
	 **/
	OFFLINE(3),
	;


	private Integer num;

	@Override
	public Integer value() {
		return num;
	}

	private PromotionStatusEnum(Integer num) {
		this.num = num;
	}

}
