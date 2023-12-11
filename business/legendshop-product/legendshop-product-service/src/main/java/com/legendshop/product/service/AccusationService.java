/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.IllegalProductBO;
import com.legendshop.product.dto.AccusationDTO;
import com.legendshop.product.dto.AccusationUserTypeDTO;
import com.legendshop.product.dto.AccusationbatchHandleDTO;
import com.legendshop.product.excel.IllegalExportDTO;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.query.ProductQuery;

import java.util.List;

/**
 * 举报服务.
 *
 * @author legendshop
 */
public interface AccusationService extends BaseService<AccusationDTO> {

	AccusationBO getAccusation(Long id);

	/**
	 * 查询我的举报
	 *
	 * @param accusationQuery
	 * @return
	 */
	PageSupport<AccusationBO> queryMyAccusation(AccusationQuery accusationQuery);

	PageSupport<AccusationBO> queryAccusation(AccusationReportQuery accusationQuery);

	/**
	 * 批量处理举报
	 *
	 * @param accusationDTO
	 * @return
	 */
	R batchHandle(AccusationbatchHandleDTO accusationDTO);

	/**
	 * 查询举报商品
	 *
	 * @param query
	 * @return
	 */
	PageSupport<AccusationBO> shopPage(AccusationQuery query);

	/**
	 * 违规商品查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<IllegalProductBO> illegalPage(ProductQuery query);

	/**
	 * 导出违规商品
	 *
	 * @param query
	 * @return
	 */
	List<IllegalExportDTO> findIllegalExport(ProductQuery query);

	/**
	 * 平台保存违规信息下架商品
	 *
	 * @param accusationDTO
	 * @return
	 */
	R saveAccusationAndOffLine(AccusationDTO accusationDTO, AccusationUserTypeDTO userTypeDTO);

	/**
	 * 用户提交举报
	 *
	 * @param accusationDTO
	 * @param userTypeDTO
	 * @return
	 */
	R saveAccusation(AccusationDTO accusationDTO, AccusationUserTypeDTO userTypeDTO);
}
