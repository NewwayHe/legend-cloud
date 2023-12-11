/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.service;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.util.SqlUtil;

import java.util.List;

/**
 * 顶级service
 *
 * @author legendshop
 */
public interface BaseService<D> {

	R save(D dto);

	R save(List<D> dto);

	/**
	 * 更新对象
	 *
	 * @param dto
	 * @return
	 */
	Boolean update(D dto);

	/**
	 * 根据id获取对象dto
	 *
	 * @param id
	 * @return
	 */
	default D getById(Long id) {
		return (D) getConvert().to(getBaseMapper().getById(id));
	}

	/**
	 * 根据id删除对象
	 *
	 * @param id
	 * @return
	 */
	default Boolean deleteById(Long id) {
		return SqlUtil.retBool(getBaseMapper().deleteById(id));
	}

	/**
	 * 根据对象删除
	 * 需要清除缓存
	 *
	 * @param d
	 * @return
	 */
	default Boolean delete(D d) {
		return SqlUtil.retBool(getBaseMapper().delete(getConvert().from(d)));
	}

	/**
	 * 获取对应dao的操作类
	 *
	 * @return genericDao
	 */
	GenericDao getBaseMapper();

	/**
	 * 获取对应convert转换类
	 *
	 * @return baseConverter
	 */
	BaseConverter getConvert();

}
