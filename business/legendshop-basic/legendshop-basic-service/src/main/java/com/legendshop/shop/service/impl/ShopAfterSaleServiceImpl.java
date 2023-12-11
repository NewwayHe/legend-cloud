/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dao.ShopAfterSaleDao;
import com.legendshop.shop.dto.ShopAfterSaleDTO;
import com.legendshop.shop.query.ShopAfterSaleQuery;
import com.legendshop.shop.service.ShopAfterSaleService;
import com.legendshop.shop.service.convert.ShopAfterSaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
public class ShopAfterSaleServiceImpl implements ShopAfterSaleService {

	@Autowired
	private ShopAfterSaleDao shopAfterSaleDao;

	@Autowired
	private ShopAfterSaleConverter shopAfterSaleConverter;

	@Override
	@Cacheable(value = "ShopAfterSaleDTO")
	public ShopAfterSaleDTO getShopAfterSale(Long id) {
		return shopAfterSaleConverter.to(shopAfterSaleDao.getById(id));
	}

	@Override
	@CacheEvict(value = "ShopAfterSaleDTO")
	public int deleteShopAfterSale(Long id) {
		return shopAfterSaleDao.deleteById(id);
	}

	@Override
	@CacheEvict(value = "ShopAfterSaleDTO", key = "#shopAfterSale.id")
	public Long saveShopAfterSale(ShopAfterSaleDTO shopAfterSaleDto) {
		Long id = null;
		if (ObjectUtil.isNotEmpty(shopAfterSaleDto.getId())) {
			id = shopAfterSaleDto.getId();
			shopAfterSaleDao.update(shopAfterSaleConverter.from(shopAfterSaleDto));
		} else {
			id = shopAfterSaleDao.save(shopAfterSaleConverter.from(shopAfterSaleDto));
		}
		return id;
	}

	@Override
	@CacheEvict(value = "ShopAfterSaleDTO", key = "#shopAfterSale.id")
	public int updateShopAfterSale(ShopAfterSaleDTO shopAfterSaleDto) {
		return shopAfterSaleDao.update(shopAfterSaleConverter.from(shopAfterSaleDto));
	}

	@Override
	@CacheEvict(value = "ShopAfterSaleDTO", key = "#id")
	public int deleteByShopAfterSaleId(Long id) {
		return shopAfterSaleDao.deleteById(id);
	}

	@Override
	public PageSupport<ShopAfterSaleDTO> getShopAfterSale(ShopAfterSaleQuery shopAfterSaleQuery) {
		return shopAfterSaleConverter.page(shopAfterSaleDao.getShopAfterSale(shopAfterSaleQuery));
	}

	@Override
	public PageSupport<ShopAfterSaleDTO> getShopAfterSalePage(ShopAfterSaleQuery shopAfterSaleQuery) {
		return shopAfterSaleConverter.page(shopAfterSaleDao.getShopAfterSale(shopAfterSaleQuery));

	}

}
