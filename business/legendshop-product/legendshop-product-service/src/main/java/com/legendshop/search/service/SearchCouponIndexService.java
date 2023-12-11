/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;


import com.legendshop.common.core.constant.R;

import java.io.IOException;

/**
 * @author legendshop
 */
public interface SearchCouponIndexService {


	/**
	 * 初始化所有优惠卷索引
	 *
	 * @return
	 */
	String initAllCouponIndex() throws IOException;

	/**
	 * 删除所有优惠卷索引
	 *
	 * @return
	 */
	Boolean delAllCouponIndex();

	/**
	 * 根据优惠卷Id重建优惠卷索引
	 *
	 * @return
	 */
	R<Void> initByCouponIdToCouponIndex(Long couponId);

	/**
	 * 根据优惠卷id删除优惠卷索引
	 *
	 * @return
	 */
	R<Void> delByCouponIdToCouponIndex(Long couponId);


}
