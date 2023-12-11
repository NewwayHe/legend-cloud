/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.bo.ProductCommentStatisticsBO;
import com.legendshop.product.dao.ProductCommentStatisticsDao;
import com.legendshop.product.service.ProductCommentStatisticsService;
import com.legendshop.product.service.convert.ProductCommentStatisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评论统计ServiceImpl.
 *
 * @author legendshop
 */
@Service
public class ProductCommentStatisticsServiceImpl implements ProductCommentStatisticsService {

	@Autowired
	private ProductCommentStatisticsDao productCommentStatDao;

	@Autowired
	private ProductCommentStatisticsConverter productCommentStatConverter;


	@Override
	public ProductCommentStatisticsBO getProductCommentStatByShopId(Long shopId) {
		return productCommentStatConverter.convert2BO(productCommentStatDao.getProductCommentStatByShopId(shopId));
	}

}
