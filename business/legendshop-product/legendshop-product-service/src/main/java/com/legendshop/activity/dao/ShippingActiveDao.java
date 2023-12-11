/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.entity.ShippingActive;
import com.legendshop.activity.query.ShippingActiveQuery;

import java.util.List;

/**
 * 包邮活动Dao
 *
 * @author legendshop
 */
public interface ShippingActiveDao extends GenericDao<ShippingActive, Long> {

	/**
	 * 保存包邮活动的方法。
	 *
	 * @param shippingActive 包邮活动对象
	 * @return 包邮活动ID
	 */
	Long saveShippingActive(ShippingActive shippingActive);

	/**
	 * 获取包邮活动列表的方法。
	 *
	 * @param query 包邮活动查询对象
	 * @return 包邮活动列表的分页支持对象
	 */
	PageSupport<ShippingActive> queryShippingActiveList(ShippingActiveQuery query);

	/**
	 * 根据ID和店铺ID获取包邮活动的方法。
	 *
	 * @param activeId 包邮活动ID
	 * @param shopId   店铺ID
	 * @return 包邮活动对象
	 */
	ShippingActive getByIdAndShopId(Long activeId, Long shopId);

	/**
	 * 查询店铺的规则引擎包邮活动列表的方法。
	 *
	 * @param shopId 店铺ID
	 * @return 规则引擎包邮活动列表
	 */
	List<ShippingActive> queryShopRuleEngine(Long shopId);

	/**
	 * 查询店铺中指定商品的在线包邮活动列表的方法。
	 *
	 * @param shopId    店铺ID
	 * @param productId 商品ID
	 * @return 在线包邮活动列表
	 */
	List<ShippingActive> queryOnLine(Long shopId, Long productId);


}
