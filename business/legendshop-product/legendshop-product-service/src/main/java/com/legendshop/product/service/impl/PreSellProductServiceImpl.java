/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.product.dao.PreSellProductDao;
import com.legendshop.product.dto.PreSellProductDTO;
import com.legendshop.product.service.PreSellProductService;
import com.legendshop.product.service.convert.PreSellProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预售商品表(PreSellProduct)表服务实现类
 *
 * @author legendshop
 * @since 2020-08-18 10:14:17
 */
@Service
public class PreSellProductServiceImpl implements PreSellProductService {

	@Autowired
	private PreSellProductDao presellProductDao;

	@Autowired
	private PreSellProductConverter converter;


	@Override
	public PreSellProductDTO getById(Long id) {
		return null;
	}

	@Override
	public int deleteById(Long id, Long prodId) {
		return 0;
	}

	@Override
	public int deleteById(Long id) {
		return 0;
	}

	@Override
	public Long save(PreSellProductDTO presellProductDTO) {
		return null;
	}

	@Override
	public int update(PreSellProductDTO presellProductDTO) {
		return 0;
	}

	@Override
	public PreSellProductDTO getById(String schemeName, Long shopId) {
		return null;
	}

	@Override
	public PreSellProductDTO getByProductId(Long preSellProdId) {

		return converter.to(presellProductDao.getByProductId(preSellProdId));
	}

}
