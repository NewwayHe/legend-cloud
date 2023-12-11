/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import com.legendshop.shop.dao.ShopCategoryCommDao;
import com.legendshop.shop.dto.ShopCategoryCommDTO;
import com.legendshop.shop.service.ShopCategoryCommService;
import com.legendshop.shop.service.convert.ShopCategoryCommConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author legendshop
 */
@Service
public class ShopCategoryCommServiceImpl implements ShopCategoryCommService {

	@Autowired
	private ShopCategoryCommDao shopCategoryCommDao;

	@Autowired
	private ShopCategoryCommConverter converter;

	@Override
	public List<ShopCategoryCommDTO> getShopCategoryCommListByShopId(Long shopId) {
		return converter.to(shopCategoryCommDao.getShopCategoryCommListByShopId(shopId));
	}

	@Override
	public int delShopCategoryComm(Long id) {
		return shopCategoryCommDao.deleteById(id);
	}

	@Override
	public Long saveShopCategoryComm(ShopCategoryCommDTO shopCategoryCommDTO) {
		return shopCategoryCommDao.save(converter.from(shopCategoryCommDTO));
	}

	@Override
	public ShopCategoryCommDTO getShopCategoryComm(Long categoryId, Long shopId) {
		return converter.to(shopCategoryCommDao.getShopCategoryComm(categoryId, shopId));
	}

}
