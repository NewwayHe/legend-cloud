/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.dao.ShopParamItemDao;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.entity.ShopParamItem;
import com.legendshop.shop.service.ShopParamItemService;
import com.legendshop.shop.service.convert.ShopParamItemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家配置项(ShopParamItem)表服务实现类
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
@Service
public class ShopParamItemServiceImpl extends BaseServiceImpl<ShopParamItemDTO, ShopParamItemDao, ShopParamItemConverter> implements ShopParamItemService {

	@Autowired
	private ShopParamItemDao shopParamItemDao;

	@Override
	public void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOS) {
		shopParamItemDao.updateValueOnlyById(sysParamValueItemDTOS);
	}

	@Override
	public List<ShopParamItemDTO> getByParentParamName(String shopParamName, Long shopId) {
		return shopParamItemDao.queryByParentParamName(shopParamName, shopId);
	}

	@Override
	public <T> T getConfigDtoByParentId(Long id, Class<T> clazz) {
		List<ShopParamItem> paramItems = shopParamItemDao.getListByParentId(id);

		Map<String, String> map = new HashMap<>(16);
		paramItems.forEach(item -> {
			map.put(item.getKeyWord(), item.getValue());
		});

		return BeanUtil.mapToBean(map, clazz, true);
	}
}
