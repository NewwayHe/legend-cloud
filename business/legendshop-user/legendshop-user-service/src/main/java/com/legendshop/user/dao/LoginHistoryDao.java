/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.entity.LoginHistory;
import com.legendshop.user.query.LoginHistoryQuery;

/**
 * 登录历史Dao.
 *
 * @author legendshop
 */
public interface LoginHistoryDao {

	/**
	 * 根据 用户名 查取用户上一次登录时间
	 **/
	LoginHistory getLastLoginTime(String userName);

	/**
	 * 保存登录历史
	 **/
	Long saveLoginHistory(LoginHistory LoginHistory);

	PageSupport<LoginHistory> getLoginHistoryPage(LoginHistoryQuery loginHistoryQuery);

}
