/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.shop.dao.ShopParamsDao;
import com.legendshop.shop.entity.ShopParams;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商家主配置(ShopParams)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-11-03 11:00:07
 */
@Repository
public class ShopParamsDaoImpl extends GenericDaoImpl<ShopParams, Long> implements ShopParamsDao {

	@Override
	public List<ShopParams> queryByShopId(Long shopId) {
		return queryByProperties(new EntityCriterion().eq("shopId", shopId));
	}

	@Override
	public ShopParams getByName(Long shopId, String name) {
		return getByProperties(new EntityCriterion().eq("name", name).eq("shopId", shopId));
	}
}
