/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.entity.ParamGroup;
import com.legendshop.product.query.ParamGroupQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface ParamGroupDao extends GenericDao<ParamGroup, Long> {

	/**
	 * 分页查询参数组信息的方法。
	 *
	 * @param paramGroupQuery 参数组查询对象
	 * @return 包含参数组信息的分页支持对象
	 */
	PageSupport<ParamGroup> getParamGroupPage(ParamGroupQuery paramGroupQuery);

	/**
	 * 根据参数组ID查询参数属性ID集合的方法。
	 *
	 * @param id 参数组ID
	 * @return 参数属性ID集合
	 */
	List<Long> getPropIdByGroupId(Long id);


	/**
	 * 查询所有在线的参数组
	 *
	 * @return
	 */
	List<ParamGroupBO> queryAllOnline();

	List<ProductPropertyBO> getPropByGroupId(Long id);
}
