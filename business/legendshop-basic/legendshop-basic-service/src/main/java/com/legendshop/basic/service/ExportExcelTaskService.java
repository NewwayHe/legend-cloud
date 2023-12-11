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
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.query.ExportExcelTaskQuery;
import com.legendshop.common.core.constant.R;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelTaskService
 * @date 2022/4/26 11:59
 * @description：
 */
public interface ExportExcelTaskService {

	/**
	 * 保存导出记录
	 *
	 * @param exportExcelTaskDTO
	 * @return 文件名
	 */
	R<String> save(ExportExcelTaskDTO exportExcelTaskDTO);

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	R<PageSupport<ExportExcelTaskDTO>> page(ExportExcelTaskQuery query);

	/**
	 * 下载文件
	 *
	 * @param servletResponse
	 * @param exportId
	 */
	void download(HttpServletResponse servletResponse, Long exportId);
}
