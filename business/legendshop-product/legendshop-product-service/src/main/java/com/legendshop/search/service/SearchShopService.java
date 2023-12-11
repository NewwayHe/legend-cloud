/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import com.legendshop.search.dto.ShopDocumentDTO;

/**
 * @author legendshop
 */
public interface SearchShopService {
	/**
	 * 更具店铺ID查找店铺
	 *
	 * @param shopId 店铺ID
	 * @return
	 */
	ShopDocumentDTO getShopById(Long shopId);


}
