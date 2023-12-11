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
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.entity.ExportExcelTask;
import com.legendshop.basic.query.ExportExcelTaskQuery;
import com.legendshop.basic.query.ExportExeclTaskQuery;

import java.util.List;

/**
 * 导出详情记录表(ExeclDetail)表数据库访问层
 *
 * @author legendshop
 * @since 2021-12-14 19:05:29
 */
public interface ExportExcelTaskDao extends GenericDao<ExportExcelTask, Long> {

	/**
	 * 获取文件记录
	 *
	 * @param id
	 * @param userId
	 * @param userType
	 * @return
	 */
	ExportExcelTask getById(Long id, Long userId, String userType);

	/**
	 * 更新导出状态
	 *
	 * @param fileName
	 * @param status
	 * @param url
	 * @return
	 */
	Integer updateStatus(String fileName, Integer status, String url, String remark);

	/**
	 * 根据用户id和导出类型查找导出历史
	 *
	 * @param exportExeclTaskQuery
	 * @return
	 */
	List<ExportExcelTaskDTO> queryByUserIdAndBusiness(ExportExeclTaskQuery exportExeclTaskQuery);

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ExportExcelTaskDTO> page(ExportExcelTaskQuery query);
}
