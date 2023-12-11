/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.pay.dao.ShopIncomingDao;
import com.legendshop.pay.dto.ShopIncomingDTO;
import com.legendshop.pay.service.ShopIncomingService;
import com.legendshop.pay.service.convert.ShopIncomingConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商家进件表(ShopIncoming)表服务实现类
 *
 * @author legendshop
 * @since 2021-03-12 09:27:59
 */
@Service
public class ShopIncomingServiceImpl extends BaseServiceImpl<ShopIncomingDTO, ShopIncomingDao, ShopIncomingConverter> implements ShopIncomingService {

	@Autowired
	private ShopIncomingConverter converter;

	@Autowired
	private ShopIncomingDao shopIncomingDao;

	@Override
	public R<ShopIncomingDTO> getByShopId(Long shopId) {
		return R.ok(converter.to(shopIncomingDao.getByShopId(shopId)));
	}
}
