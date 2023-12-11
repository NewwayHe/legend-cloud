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
import com.legendshop.shop.dao.ShopBannerDao;
import com.legendshop.shop.dto.ShopBannerDTO;
import com.legendshop.shop.service.ShopBannerService;
import com.legendshop.shop.service.convert.ShopBannerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商家默认的轮换图服务
 *
 * @author legendshop
 */
@Service("shopBannerService")
public class ShopBannerServiceImpl implements ShopBannerService {

	@Autowired
	private ShopBannerDao shopBannerDao;

	@Autowired
	private ShopBannerConverter converter;


	@Override
	public ShopBannerDTO getShopBanner(Long id) {
		return converter.to(shopBannerDao.getById(id));
	}

	@Override
	public int deleteShopBanner(Long id, String imageFile) {
		return shopBannerDao.deleteById(id);
	}

	@Override
	public Long saveShopBanner(ShopBannerDTO shopBannerDTO) {
		if (ObjectUtil.isNotEmpty(shopBannerDTO.getId())) {
			updateShopBanner(shopBannerDTO);
			return shopBannerDTO.getId();
		}
		return shopBannerDao.save(converter.from(shopBannerDTO));
	}

	@Override
	public int updateShopBanner(ShopBannerDTO shopBannerDTO) {
		return shopBannerDao.update(converter.from(shopBannerDTO));
	}

	@Override
	public PageSupport<ShopBannerDTO> getShopBanner(String curPageNo, Long shopId) {
		return converter.page(shopBannerDao.getShopBanner(curPageNo, shopId));
	}
}
