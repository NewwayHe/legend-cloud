/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.shop.dto.ShopParamItemDTO;

import java.util.List;

/**
 * 商家配置项(ShopParamItem)表服务接口
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
public interface ShopParamItemService extends BaseService<ShopParamItemDTO> {

	/**
	 * 修改配置项的value
	 *
	 * @param sysParamValueItemDTOS
	 */
	void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOS);

	List<ShopParamItemDTO> getByParentParamName(String shopParamName, Long shopId);

	<T> T getConfigDtoByParentId(Long id, Class<T> clazz);
}
