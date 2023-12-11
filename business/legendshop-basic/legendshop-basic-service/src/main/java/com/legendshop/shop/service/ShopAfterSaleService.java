/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.ShopAfterSaleDTO;
import com.legendshop.shop.query.ShopAfterSaleQuery;

/**
 * @author legendshop
 */
public interface ShopAfterSaleService {

	ShopAfterSaleDTO getShopAfterSale(Long id);

	int deleteShopAfterSale(Long id);

	Long saveShopAfterSale(ShopAfterSaleDTO shopAfterSaleDto);

	int updateShopAfterSale(ShopAfterSaleDTO shopAfterSaleDto);

	int deleteByShopAfterSaleId(Long id);

	PageSupport<ShopAfterSaleDTO> getShopAfterSale(ShopAfterSaleQuery shopAfterSaleQuery);

	PageSupport<ShopAfterSaleDTO> getShopAfterSalePage(ShopAfterSaleQuery shopAfterSaleQuery);
}
