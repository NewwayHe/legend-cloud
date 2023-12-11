/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.UserOperationLogDTO;
import com.legendshop.basic.query.UserOperationLogQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface UserOperationLogService {
	void save(UserOperationLogDTO log);

	List<UserOperationLogDTO> findOperationType(String type);

	PageSupport<UserOperationLogDTO> findDataPageList(UserOperationLogQuery query);
}
