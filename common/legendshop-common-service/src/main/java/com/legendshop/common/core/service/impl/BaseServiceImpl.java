/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.common.core.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基础泛型service的实现类
 *
 * @param <D> DTO
 * @param <M> mapper，也就是dao
 * @param <C> convert对象
 * @author legendshop
 */
public class BaseServiceImpl<D extends BaseDTO, M extends GenericDao, C extends BaseConverter> implements BaseService<D> {

	@Autowired
	private M genericDao;

	@Autowired
	private C baseConverter;

	/**
	 * 保存对象
	 *
	 * @param dto
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public R save(D dto) {
		dto.setCreateTime(DateUtil.date());
		return R.ok(SqlUtil.retBool(Integer.parseInt(getBaseMapper().save(getConvert().from(dto)).toString())));
	}


	/**
	 * 批量保存
	 *
	 * @param dto
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public R save(List<D> dto) {
		dto.forEach(e -> e.setCreateTime(DateUtil.date()));
		List save = getBaseMapper().save(getConvert().from(dto));
		return R.ok(SqlUtil.retBool(save.size()));
	}


	@Override
	public Boolean update(D dto) {
		Object obj = genericDao.getById(dto.getId());
		if (obj == null) {
			return Boolean.FALSE;
		}
		dto.setUpdateTime(DateUtil.date());
		BeanUtil.copyProperties(dto, obj, new CopyOptions().setIgnoreNullValue(Boolean.TRUE));
		return SqlUtil.retBool(genericDao.update(obj));
	}


	@Override
	public GenericDao getBaseMapper() {
		return genericDao;
	}

	@Override
	public BaseConverter getConvert() {
		return baseConverter;
	}
}
