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
 * 文章位置枚举
 * LegendShop 版权所有 2009-2011,并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得LegendShop商业授权之前，您不能将本软件应用于商业用途，否则LegendShop将保留追究的权力。
 * ----------------------------------------------------------------------------.
 *
 * @author legendshop
 */
public enum NewsPositionEnum implements IntegerEnum {

	// 普通文章
	/**
	 * The NEW s_ news.
	 */
	NEWS_NEWS(1),

	// 顶部文章
	/**
	 * The NEW s_ top.
	 */
	NEWS_TOP(0),

	// 分类文章
	/**
	 * The NEW s_ sort.
	 */
	NEWS_SORT(3),

	// 底部文章
	/**
	 * The NEW s_ bottom.
	 */
	NEWS_BOTTOM(4),

	// 网站底部文章
	/**
	 * The NEW s_ bottom.
	 */
	NEWS_MOSTBOTTOM(-1),

	// 团购上部文章
	NEWS_GROUP_TOP(2),

	// 团购下部文章
	NEWS_GROUP_BOTTOM(5),

	//App的文章
	NEWS_APP(-2);

	/**
	 * The num.
	 */
	private Integer num;

	/**
	 * (non-Javadoc)
	 *
	 * @see com.legendshop.core.constant.IntegerEnum#value()
	 **/
	@Override
	public Integer value() {
		return num;
	}

	/**
	 * Instantiates a new news category status enum.
	 *
	 * @param num the num
	 */
	NewsPositionEnum(Integer num) {
		this.num = num;
	}

}
