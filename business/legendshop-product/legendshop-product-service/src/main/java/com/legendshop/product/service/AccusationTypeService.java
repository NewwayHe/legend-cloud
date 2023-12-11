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
import com.legendshop.product.dto.AccusationTypeDTO;
import com.legendshop.product.query.AccusationTypeQuery;

import java.util.List;

/**
 * 举报类型服务.
 *
 * @author legendshop
 */
public interface AccusationTypeService extends BaseService<AccusationTypeDTO> {

	/**
	 * 批量更新状态
	 *
	 * @param ids    ID列表
	 * @param status 状态
	 * @return 结果
	 */
	R batchUpdateStatus(List<Long> ids, Integer status);

	/**
	 * 批量删除
	 *
	 * @param longList 待删除数据ID列表
	 * @return 结果
	 */
	R batchDelete(List<Long> longList);

	/**
	 * 分页查询举报类型
	 *
	 * @param query 查询条件
	 * @return 分页结果
	 */
	PageSupport<AccusationTypeDTO> queryPage(AccusationTypeQuery query);

	/**
	 * 查询所有在线举报类型
	 *
	 * @return 在线举报类型列表
	 */
	List<AccusationTypeDTO> queryAllOnLine();

	/**
	 * 删除举报类型
	 *
	 * @param id 要删除的举报类型ID
	 * @return 结果
	 */
	R deleteAccusationType(Long id);

}
