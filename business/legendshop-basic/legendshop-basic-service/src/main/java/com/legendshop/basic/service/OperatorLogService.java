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
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.OperatorLogDTO;

/**
 * 操作日志表(OperatorLog)表服务接口
 *
 * @author legendshop
 * @since 2023-08-29 14:13:57
 */
public interface OperatorLogService {

	/**
	 * 保存操作日志
	 *
	 * @param operatorLogDTO
	 * @return
	 */
	R save(OperatorLogDTO operatorLogDTO);

	/**
	 * 查询操作日志
	 *
	 * @param operatorLogQuery
	 * @return
	 */
	R<PageSupport<OperatorLogDTO>> page(OperatorLogQuery operatorLogQuery);

	/***
	 * 查看详情
	 * @param id
	 * @return
	 */
	R<OperatorLogDTO> getById(Long id);

}
