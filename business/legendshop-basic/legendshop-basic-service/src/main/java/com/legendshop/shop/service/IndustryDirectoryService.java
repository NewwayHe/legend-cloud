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
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import com.legendshop.shop.query.IndustryDirectoryQuery;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表服务接口
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
public interface IndustryDirectoryService {

	/**
	 * 分页获取行业目录列表
	 *
	 * @param query 查询对象
	 * @return 分页结果
	 */
	R<PageSupport<IndustryDirectoryDTO>> pageList(IndustryDirectoryQuery query);

	/**
	 * 获取可用的行业目录列表
	 *
	 * @param query 查询对象
	 * @return 可用的行业目录列表
	 */
	R<List<IndustryDirectoryDTO>> availableList(IndustryDirectoryQuery query);

	/**
	 * 根据ID删除行业目录
	 *
	 * @param id 行业目录ID
	 * @return 删除结果
	 */
	R<Void> deleteById(Long id);

	/**
	 * 保存行业目录信息
	 *
	 * @param industryDirectoryDTO 行业目录DTO
	 * @return 无
	 */
	R<Void> save(IndustryDirectoryDTO industryDirectoryDTO);

	/**
	 * 更新行业目录信息
	 *
	 * @param industryDirectoryDTO 行业目录DTO
	 * @return 无
	 */
	R<Void> update(IndustryDirectoryDTO industryDirectoryDTO);

	/**
	 * 根据ID获取行业目录详情
	 *
	 * @param id 行业目录ID
	 * @return 行业目录DTO
	 */
	IndustryDirectoryDTO getById(Long id);

	/**
	 * 根据ID列表查询行业目录列表
	 *
	 * @param ids ID列表
	 * @return 行业目录列表
	 */
	List<IndustryDirectoryDTO> queryById(List<Long> ids);

	/**
	 * 获取所有行业目录列表
	 *
	 * @return 所有行业目录列表
	 */
	List<IndustryDirectoryDTO> queryAll();
}
