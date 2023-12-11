/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dao.AppStartAdvDao;
import com.legendshop.shop.entity.AppStartAdv;
import com.legendshop.shop.query.AppStartAdvQuery;
import org.springframework.stereotype.Repository;

/**
 * App启动广告Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class AppStartAdvDaoImpl extends GenericDaoImpl<AppStartAdv, Long> implements AppStartAdvDao {

	/**
	 * 根据ID更新状态
	 */
	public static final String SQL_UPDATE_STATUS_BY_ID = "UPDATE ls_app_start_adv SET status = ? WHERE id = ?";

	/**
	 * 根据name统计记录数
	 */
	public static final String SQL_GET_COUNT_BY_NAME = "SELECT COUNT(1) FROM ls_app_start_adv WHERE name = ?";

	/**
	 * 根据Id查询name
	 */
	public static final String SQL_GET_NAME_BY_NAME = "SELECT name FROM ls_app_start_adv WHERE id = ?";


	@Override
	public int getCount(String name) {
		int result = get(SQL_GET_COUNT_BY_NAME, Integer.class, name);
		return result;
	}

	@Override
	public String getName(Long id) {
		return get(SQL_GET_NAME_BY_NAME, String.class, id);
	}

	/**
	 * 启动广告分页查询
	 *
	 * @param appStartAdvQuery
	 * @return
	 */
	@Override
	public PageSupport<AppStartAdv> page(AppStartAdvQuery appStartAdvQuery) {
		CriteriaQuery cq = new CriteriaQuery(AppStartAdv.class, appStartAdvQuery.getPageSize(), appStartAdvQuery.getCurPage());
		cq.like("name", appStartAdvQuery.getName(), MatchMode.ANYWHERE);
		cq.eq("status", appStartAdvQuery.getStatus());
		cq.addDescOrder("status");
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	/**
	 * 修改启动广告状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public int updateStatus(Long id, Integer status) {
		return update(SQL_UPDATE_STATUS_BY_ID, status, id);
	}

}
