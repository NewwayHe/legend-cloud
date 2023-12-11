/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.UserOperationLogDao;
import com.legendshop.basic.dto.UserOperationLogDTO;
import com.legendshop.basic.entity.UserOperationLog;
import com.legendshop.basic.query.UserOperationLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class UserOperationLogDaoImpl extends GenericDaoImpl<UserOperationLog, Long> implements UserOperationLogDao {

	@Override
	public List<UserOperationLogDTO> findOperationType(String type) {
		return super.query("SELECT uol.code AS code, MAX(uol.name) AS name FROM ls_user_operation_log uol WHERE type = ? GROUP BY uol.code;", UserOperationLogDTO.class, type);
	}

	@Override
	public PageSupport<UserOperationLog> findDataPageList(UserOperationLogQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(UserOperationLog.class, query.getPageSize(), query.getCurPage());
		if (StringUtils.isNotBlank(query.getMobile())) {
			criteriaQuery.eq("userMobile", query.getMobile());
		}

		if (StringUtils.isNotBlank(query.getUsername())) {
			criteriaQuery.eq("userName", query.getUsername());
		}

		if (StringUtils.isNotBlank(query.getButtonCode())) {
			criteriaQuery.eq("buttonCode", query.getButtonCode());
		}

		if (StringUtils.isNotBlank(query.getPageCode())) {
			criteriaQuery.eq("pageCode", query.getPageCode());
		}

		if (null != query.getStartTime() && null != query.getEndTime()) {
			criteriaQuery.gt("time", DateUtil.beginOfDay(query.getStartTime()));
			criteriaQuery.lt("time", DateUtil.endOfDay(query.getEndTime()));
		}
		criteriaQuery.addDescOrder("time");
		return super.queryPage(criteriaQuery);
	}
}
