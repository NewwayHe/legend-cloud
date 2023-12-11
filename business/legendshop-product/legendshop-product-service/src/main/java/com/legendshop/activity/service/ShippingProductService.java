/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.bo.FullActiveProductBO;
import com.legendshop.activity.dto.ShippingProductDTO;

import java.util.List;

/**
 * 包邮活动商品Service
 *
 * @author legendshop
 */
public interface ShippingProductService {

	ShippingProductDTO getById(Long id);

	void deleteById(Long id);

	Long save(ShippingProductDTO shippingProd);

	void update(ShippingProductDTO shippingProd);

	List<FullActiveProductBO> queryByActiveIdByShopId(Long activeId, Long shopId);

	List<ShippingProductDTO> getProdByActiveId(Long id, Long shopId);

}
