/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.dao.LoginHistoryDao;
import com.legendshop.user.entity.LoginHistory;
import com.legendshop.user.query.LoginHistoryQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 登录历史Dao
 *
 * @author legendshop
 */
@Repository
public class LoginHistoryDaoImpl extends GenericDaoImpl<LoginHistory, Long> implements LoginHistoryDao {


	@Override
	public LoginHistory getLastLoginTime(String userName) {
		List<LoginHistory> list = this.queryLimit("select id,area,country,user_name as userName,ip,time from ls_login_hist where user_name= ? order by time desc", LoginHistory.class, 1, 1, userName);
		if (CollUtil.isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public PageSupport<LoginHistory> getLoginHistoryPage(LoginHistoryQuery loginHistoryQuery) {
		CriteriaQuery cq = new CriteriaQuery(LoginHistory.class, loginHistoryQuery.getPageSize(), loginHistoryQuery.getCurPage());
		cq.ge("time", loginHistoryQuery.getStartTime());
		if (ObjectUtil.isNotEmpty(loginHistoryQuery.getEndTime())) {
		}
		cq.addOrder("desc", "time");
		return queryPage(cq);
	}


	@Override
	public Long saveLoginHistory(LoginHistory LoginHistory) {
		return save(LoginHistory);
	}

}
