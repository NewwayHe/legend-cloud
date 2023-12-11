/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.pay.dto.ShopIncomingDTO;

/**
 * 商家进件表(ShopIncoming)表服务接口
 *
 * @author legendshop
 * @since 2021-03-12 09:27:59
 */
public interface ShopIncomingService extends BaseService<ShopIncomingDTO> {

	/**
	 * 根据店铺ID获取进件信息
	 *
	 * @param shopId
	 * @return
	 */
	R<ShopIncomingDTO> getByShopId(Long shopId);
}
