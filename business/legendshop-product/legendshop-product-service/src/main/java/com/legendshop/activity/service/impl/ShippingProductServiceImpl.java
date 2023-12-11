/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;


import com.legendshop.activity.bo.FullActiveProductBO;
import com.legendshop.activity.dao.ShippingProductDao;
import com.legendshop.activity.dto.ShippingProductDTO;
import com.legendshop.activity.service.ShippingProductService;
import com.legendshop.activity.service.convert.ShippingProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 包邮活动商品Service
 *
 * @author legendshop
 */
@Service
public class ShippingProductServiceImpl implements ShippingProductService {

	@Autowired
	private ShippingProductDao shippingProductDao;

	@Autowired
	private ShippingProductConverter shippingProductConverter;

	@Override
	public ShippingProductDTO getById(Long id) {
		return shippingProductConverter.to(shippingProductDao.getById(id));
	}

	@Override
	public void deleteById(Long id) {
		shippingProductDao.deleteById(id);
	}

	@Override
	public Long save(ShippingProductDTO shippingProductDTO) {
		return shippingProductDao.save(shippingProductConverter.from(shippingProductDTO));
	}

	@Override
	public void update(ShippingProductDTO shippingProductDTO) {
		shippingProductDao.update(shippingProductConverter.from(shippingProductDTO));
	}

	@Override
	public List<FullActiveProductBO> queryByActiveIdByShopId(Long activeId, Long shopId) {
		return shippingProductDao.queryByActiveIdByShopId(activeId, shopId);
	}

	@Override
	public List<ShippingProductDTO> getProdByActiveId(Long id, Long shopId) {
		return shippingProductConverter.to(shippingProductDao.queryByShippingIdAndShopId(id, shopId));
	}
}
