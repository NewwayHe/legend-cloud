/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 安全等级 Enum
 *
 * @author legendshop
 */
public enum UserSecurityEnum implements IntegerEnum {

	/**
	 * 不安全 (Level 1)
	 */
	SECURITY_DANGEROUS(1),

	/**
	 * 有点不安全 (Level 2)
	 */
	SECURITY_LESS_DANGEROUS(2),

	/**
	 * 安全 (Level 3)
	 */
	SECURITY_SAFETY(3),

	/**
	 * 比较安全 (Level 4)
	 */
	SECURITY_MORE_SAFETY(4);


	/**
	 * The num.
	 */
	private Integer num;

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.IntegerEnum#value()
	 */
	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new shop status enum.
	 *
	 * @param num the num
	 */
	UserSecurityEnum(Integer num) {
		this.num = num;
	}

}
