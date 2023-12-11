/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.mq.producer;

import java.util.Date;

/**
 * @author legendshop
 */
public interface ProductProducerService {

	/**
	 * 商品预约上架
	 *
	 * @param productId 商品id
	 * @param time      上架日期
	 */
	void appointOnLine(Long productId, Date time);

	/**
	 * 预售结束队列
	 *
	 * @param productId 商品id
	 * @param time      上架日期
	 */
	void preSellFinish(Long productId, Date time);
}
