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
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.basic.dao.ExportExcelTaskDao;
import com.legendshop.basic.dto.ExportExcelTaskDTO;
import com.legendshop.basic.entity.ExportExcelTask;
import com.legendshop.basic.query.ExportExcelTaskQuery;
import com.legendshop.basic.query.ExportExeclTaskQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导出详情记录表(ExeclDetail)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-12-14 19:05:29
 */
@Repository
public class ExportExcelTaskDaoImpl extends GenericDaoImpl<ExportExcelTask, Long> implements ExportExcelTaskDao {

	@Override
	public ExportExcelTask getById(Long id, Long userId, String userType) {
		LambdaEntityCriterion<ExportExcelTask> criterion = new LambdaEntityCriterion<>(ExportExcelTask.class);
		criterion.eq(ExportExcelTask::getId, id);
		criterion.eq(ExportExcelTask::getUserId, userId);
		criterion.eq(ExportExcelTask::getUserType, userType);
		return getByProperties(criterion);
	}

	@Override
	public Integer updateStatus(String fileName, Integer status, String url, String remark) {
		return update("update ls_export_excel_task set status = ?, url = ? , update_time = ?, remark = ? where file_name = ?", status, url, DateUtil.date(), remark, fileName);
	}

	@Override
	public List<ExportExcelTaskDTO> queryByUserIdAndBusiness(ExportExeclTaskQuery exportExeclTaskQuery) {
		return query("select * from ls_export_excel_task where user_id = ? and business_name = ? order by create_time desc limit 1",
				ExportExcelTaskDTO.class, exportExeclTaskQuery.getUserId(), exportExeclTaskQuery.getBusinessName());
	}

	@Override
	public PageSupport<ExportExcelTaskDTO> page(ExportExcelTaskQuery query) {
		LambdaCriteriaQuery<ExportExcelTaskDTO> criteriaQuery = new LambdaCriteriaQuery<>(ExportExcelTaskDTO.class, query);
		criteriaQuery.eq(ExportExcelTaskDTO::getUserId, query.getUserId());
		criteriaQuery.eq(ExportExcelTaskDTO::getUserType, query.getUserType());
		criteriaQuery.addDescOrder(ExportExcelTaskDTO::getCreateTime);
		return queryDTOPage(criteriaQuery);
	}
}
