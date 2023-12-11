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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.entity.AppStartAdv;
import com.legendshop.shop.query.AppStartAdvQuery;

/**
 * @author legendshop
 */
public interface AppStartAdvDao extends GenericDao<AppStartAdv, Long> {


	int getCount(String name);

	String getName(Long id);

	/**
	 * 启动广告分页查询
	 *
	 * @param appStartAdvQuery
	 * @return
	 */
	PageSupport<AppStartAdv> page(AppStartAdvQuery appStartAdvQuery);

	/**
	 * 修改启动广告状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	int updateStatus(Long id, Integer status);

}
