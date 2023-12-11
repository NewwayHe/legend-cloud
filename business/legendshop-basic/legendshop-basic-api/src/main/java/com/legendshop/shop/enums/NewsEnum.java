/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.enums;

import com.legendshop.common.core.enums.IntegerEnum;

/**
 * @author legendshop
 */
public enum NewsEnum implements IntegerEnum {


	/**
	 * 新闻不高亮
	 */
	NEWS_HighLine_NO(0),

	/**
	 * 新闻高亮
	 */
	NEWS_HighLine_YES(1);

	/**
	 * The num.
	 */
	private Integer num;


	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new news category status enum.
	 *
	 * @param num the num
	 */
	NewsEnum(Integer num) {
		this.num = num;
	}

}
