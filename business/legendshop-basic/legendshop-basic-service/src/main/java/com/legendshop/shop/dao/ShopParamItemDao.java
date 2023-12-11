/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.entity.ShopParamItem;

import java.util.List;

/**
 * 商家配置项(ShopParamItem)表数据库访问层
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
public interface ShopParamItemDao extends GenericDao<ShopParamItem, Long> {


	void deleteByShopId(List<Long> parentId);

	/**
	 * 修改配置项的value
	 *
	 * @param sysParamValueItemDTOList
	 */
	void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOList);

	List<ShopParamItemDTO> queryByParentParamName(String shopParamName, Long shopId);

	/**
	 * 根据主配置id获取配置项
	 *
	 * @param parentId
	 * @return
	 */
	List<ShopParamItem> getListByParentId(Long parentId);
}
