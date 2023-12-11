/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.UserOperationLogDTO;
import com.legendshop.basic.entity.UserOperationLog;
import com.legendshop.basic.query.UserOperationLogQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface UserOperationLogDao extends GenericDao<UserOperationLog, Long> {
	List<UserOperationLogDTO> findOperationType(String type);

	PageSupport<UserOperationLog> findDataPageList(UserOperationLogQuery query);
}
