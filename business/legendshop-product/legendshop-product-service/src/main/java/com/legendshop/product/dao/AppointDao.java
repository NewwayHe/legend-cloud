/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.product.entity.Appoint;

import java.util.List;

/**
 * 商品预约上架(Appoint)表数据库访问层
 *
 * @author legendshop
 * @since 2020-08-12 18:09:36
 */
public interface AppointDao extends GenericDao<Appoint, Long> {

	/**
	 * 根据商品 ID 获取预约上架信息的方法。
	 *
	 * @param productId 商品 ID
	 * @return 匹配商品 ID 的预约上架信息对象
	 */
	Appoint queryByProductId(Long productId);

	/**
	 * 根据商品 ID 列表获取预约上架信息的方法。
	 *
	 * @param appointIdList 商品 ID 列表
	 * @return 匹配商品 ID 列表的预约上架信息对象列表
	 */
	List<Appoint> queryByProductId(List<Long> appointIdList);
}
