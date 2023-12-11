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
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dto.ParamGroupDTO;
import com.legendshop.product.query.ParamGroupQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 参数组服务
 *
 * @author legendshop
 */
public interface ParamGroupService {

	/**
	 * 根据ID获取参数
	 *
	 * @param id 参数ID
	 * @return
	 */
	ParamGroupBO getById(Long id);

	/**
	 * 根据ID删除参数
	 *
	 * @param id 参数ID
	 * @return
	 */
	int deleteParamGroupById(Long id);

	/**
	 * 保存参数
	 *
	 * @param paramGroupDTO
	 * @return
	 */
	Long saveParamGroup(ParamGroupDTO paramGroupDTO);

	/**
	 * 更新参数的方法。
	 *
	 * @param paramGroupDTO 要更新的参数组数据传输对象
	 * @return 更新操作影响的行数
	 */
	int updateParamGroup(ParamGroupDTO paramGroupDTO);


	/**
	 * 查询参数分页列表
	 *
	 * @param paramGroupQuery
	 * @return
	 */
	PageSupport<ParamGroupBO> getParamGroupPage(ParamGroupQuery paramGroupQuery);

	/**
	 * 根据 ID 分页查询参数属性的方法。
	 *
	 * @param query 参数组查询对象
	 * @return 包含参数属性的分页支持对象
	 */
	PageSupport<ProductPropertyBO> getParamAndValueById(ParamGroupQuery query);

	/**
	 * 根据参数组 ID 集合查询参数组、参数、参数值（三维数组）的方法。
	 *
	 * @param toList 参数组 ID 集合
	 * @return 包含参数属性的列表
	 */
	List<ProductPropertyBO> getDetailByIds(ArrayList<Long> toList);

	/**
	 * 根据分类 ID 查询参数组、参数、参数值（三维数组）的方法。
	 *
	 * @param id         分类 ID
	 * @param productId  商品 ID
	 * @return 包含参数属性的列表
	 */
	List<ProductPropertyBO> getDetailByCategoryId(long id, Long productId);

	/**
	 * 查询所有在线的参数组
	 *
	 * @return
	 */
	List<ParamGroupBO> allOnline();
}
