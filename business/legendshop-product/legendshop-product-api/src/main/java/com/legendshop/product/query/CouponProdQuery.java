/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class CouponProdQuery extends PageParams {

	private static final long serialVersionUID = 962010998394049414L;

	/**
	 * 排序条件（属性名、排序方式）
	 */
	String orders;

	/**
	 * 优惠券id
	 */
	Long couponId;

	/**
	 * 店铺Id
	 */
	Long shopId;

	/**
	 * 商品ID列表
	 */
	List<Long> productIds;

}
