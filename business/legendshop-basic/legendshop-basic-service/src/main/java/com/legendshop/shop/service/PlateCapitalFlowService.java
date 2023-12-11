/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.bo.PlateCapitalFlowAmountBO;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.excel.PlateCapitalFlowExportDTO;
import com.legendshop.shop.query.PlateCapitalFlowQuery;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台资金流水(PlateCapitalFlow)表服务接口
 *
 * @author legendshop
 * @since 2020-09-18 17:26:10
 */
public interface PlateCapitalFlowService {


	/**
	 * 查询
	 *
	 * @param id
	 * @return
	 */
	PlateCapitalFlowDTO getById(Long id);

	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	int deleteById(Long id);

	/**
	 * 保存
	 *
	 * @param plateCapitalFlowDTO
	 * @return
	 */
	Long save(PlateCapitalFlowDTO plateCapitalFlowDTO);


	/**
	 * 修改
	 *
	 * @param plateCapitalFlowDTO
	 * @return
	 */
	int update(PlateCapitalFlowDTO plateCapitalFlowDTO);


	/**
	 * 平台资金流水列表
	 *
	 * @param plateCapitalFlowQuery
	 * @return
	 */
	PageSupport<PlateCapitalFlowDTO> queryPage(PlateCapitalFlowQuery plateCapitalFlowQuery);

	/**
	 * 平台资金流水列表
	 *
	 * @param plateCapitalFlowQuery
	 * @return
	 */
	List<PlateCapitalFlowDTO> queryList(PlateCapitalFlowQuery plateCapitalFlowQuery);

	/**
	 * 导出列表
	 *
	 * @param plateCapitalFlowQuery
	 * @return
	 */
	List<PlateCapitalFlowExportDTO> queryExportList(PlateCapitalFlowQuery plateCapitalFlowQuery);

	/**
	 * 根据支出类型查询总金额
	 *
	 * @param flowType
	 * @return
	 */
	BigDecimal getSumAmount(String flowType);


	/**
	 * 获取平台金额
	 *
	 * @return
	 */
	PlateCapitalFlowAmountBO getPlateAmount();
}
