/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.bo.ProductCommentStatisticsBO;

/**
 * 评论统计服务.
 *
 * @author legendshop
 */
public interface ProductCommentStatisticsService {

	/**
	 * 根据店铺ID获取商品评论统计信息的方法。
	 *
	 * @param shopId 店铺ID
	 * @return 商品评论统计信息
	 */
	ProductCommentStatisticsBO getProductCommentStatByShopId(Long shopId);

}
