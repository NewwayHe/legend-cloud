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
import com.legendshop.shop.entity.ShopParams;

import java.util.List;

/**
 * 商家主配置(ShopParams)表数据库访问层
 *
 * @author legendshop
 * @since 2020-11-03 11:00:07
 */
public interface ShopParamsDao extends GenericDao<ShopParams, Long> {

	List<ShopParams> queryByShopId(Long shopId);

	ShopParams getByName(Long shopId, String name);
}
