/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.dao.LogisticsCompanyDao;
import com.legendshop.order.dao.OrderLogisticsDao;
import com.legendshop.order.dto.LogisticsCompanyDTO;
import com.legendshop.order.entity.LogisticsCompany;
import com.legendshop.order.query.LogisticsCompanyQuery;
import com.legendshop.order.service.LogisticsCompanyService;
import com.legendshop.order.service.convert.LogisticsCompanyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流公司的接口
 *
 * @author legendshop
 */
@Service
public class LogisticsCompanyServiceImpl implements LogisticsCompanyService {

	@Autowired
	private LogisticsCompanyDao logisticsCompanyDao;

	@Autowired
	private OrderLogisticsDao orderLogisticsDao;

	@Autowired
	private LogisticsCompanyConverter logisticsCompanyConverter;


	@Override
	@Cacheable(value = "LogisticsCompanyDetail", key = "#id", unless = "#result==null")
	public LogisticsCompanyDTO getById(Long id) {
		return logisticsCompanyConverter.to(logisticsCompanyDao.getById(id));
	}

	@Override
	@CacheEvict(value = "LogisticsCompanyDetail", key = "#id")
	public void deleteById(Long id) {
		LogisticsCompanyDTO dto = getById(id);
		if (dto.getShopId() == -1L && dto.getUseCount() > 0) {
			throw new BusinessException("该物流公司已被引用不可以删除");
		}
		if (dto.getShopId() != -1L) {
			logisticsCompanyDao.subUseCount(dto.getParentId());
		}
		logisticsCompanyDao.deleteById(id);
	}

	@Override
	public Long save(LogisticsCompanyDTO logisticsCompanyDTO) {
		logisticsCompanyDTO.setCreateTime(DateUtil.date());
		logisticsCompanyDTO.setModifyTime(DateUtil.date());
		logisticsCompanyDTO.setUseCount(0);
		return logisticsCompanyDao.save(logisticsCompanyConverter.from(logisticsCompanyDTO));
	}

	@Override
	//	@CacheEvict(value = "LogisticsCompanyDetail", key = "#id")
	public void update(LogisticsCompanyDTO logisticsCompanyDTO) {
		logisticsCompanyDTO.setModifyTime(DateUtil.date());
		logisticsCompanyDao.update(logisticsCompanyConverter.from(logisticsCompanyDTO));
	}


	@Override
	public PageSupport<LogisticsCompanyDTO> queryPage(LogisticsCompanyQuery logisticsCompanyQuery) {
		PageSupport<LogisticsCompanyDTO> page = logisticsCompanyConverter.page(logisticsCompanyDao.queryPage(logisticsCompanyQuery));
		return page;
	}

	@Override
	public List<LogisticsCompanyDTO> getLogisticsCompany() {
		return logisticsCompanyConverter.to(logisticsCompanyDao.queryAll());
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchAddLogisticsCompanyDTO(List<LogisticsCompanyDTO> list, Long shopId) {

		logisticsCompanyDao.deleteByShopId(shopId);
		list.forEach(c -> {
			Assert.notNull(c.getId(), "id不能为空！");
			c.setShopId(shopId);
			c.setParentId(c.getId());
			save(c);
			logisticsCompanyDao.addUseCount(c.getId());
		});
	}

	@Override
	public List<LogisticsCompanyDTO> getList(Long shopId) {
		return logisticsCompanyConverter.to(logisticsCompanyDao.getList(shopId));
	}

	@Override
	public List<LogisticsCompanyDTO> queryAll(Long shopId) {
		List<LogisticsCompany> logisticsCompanyList = logisticsCompanyDao.queryAllByShopId(shopId);
		return logisticsCompanyConverter.to(logisticsCompanyList);
	}


	@Override
	public List<LogisticsCompanyDTO> queryByNameList(ArrayList<String> logisticsCompanyList) {
		return this.logisticsCompanyConverter.to(this.logisticsCompanyDao.queryByNameList(logisticsCompanyList));
	}
}
