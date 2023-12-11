/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.dao.AccusationDao;
import com.legendshop.product.dao.AccusationTypeDao;
import com.legendshop.product.dto.AccusationTypeDTO;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.query.AccusationTypeQuery;
import com.legendshop.product.service.AccusationTypeService;
import com.legendshop.product.service.convert.AccusationTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 举报类型服务.
 *
 * @author legendshop
 */
@Service
public class AccusationTypeServiceImpl extends BaseServiceImpl<AccusationTypeDTO, AccusationTypeDao, AccusationTypeConverter> implements AccusationTypeService {

	@Autowired
	private AccusationTypeDao accusationTypeDao;

	@Autowired
	private AccusationDao accusationDao;

	@Autowired
	private AccusationTypeConverter converter;

	@Override
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public R save(AccusationTypeDTO dto) {
		return super.save(dto);
	}

	@Override
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public R save(List<AccusationTypeDTO> dto) {
		return super.save(dto);
	}

	@Override
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public Boolean update(AccusationTypeDTO dto) {
		return super.update(dto);
	}

	@Override
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public R deleteAccusationType(Long id) {
		AccusationReportQuery accusationQuery = new AccusationReportQuery();
		accusationQuery.setTypeId(id);
		PageSupport<AccusationBO> pageSupport = accusationDao.queryAccusation(accusationQuery);
		if (pageSupport.getTotal() > 0) {
			R.fail("删除失败，已有该举报类型的举报业务");
		}
		return R.ok(accusationTypeDao.deleteById(id));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public R batchUpdateStatus(List<Long> ids, Integer status) {
		return R.ok(accusationTypeDao.batchUpdateStatus(ids, status));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.ACCUSATIONTYPE, allEntries = true)
	public R batchDelete(List<Long> longList) {
		Integer count = accusationDao.getCountByTypeId(longList);
		if (count > 0) {
			R.fail("删除失败，已有该举报类型的举报业务");
		}
		return R.ok(accusationTypeDao.deleteById(longList));
	}

	@Override
	public PageSupport<AccusationTypeDTO> queryPage(AccusationTypeQuery query) {
		return converter.page(accusationTypeDao.queryPage(query));
	}

	@Override
	@Cacheable(value = CacheConstants.ACCUSATIONTYPE, key = "'OnLineList'", unless = "#result.isEmpty()")
	public List<AccusationTypeDTO> queryAllOnLine() {
		return converter.to(accusationTypeDao.queryAllOnLine());
	}
}
