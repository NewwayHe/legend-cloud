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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dao.ShopBillPeriodDao;
import com.legendshop.order.dto.ShopBillPeriodDTO;
import com.legendshop.order.excel.ShopBillPeriodExportDTO;
import com.legendshop.order.query.ShopBillPeriodQuery;
import com.legendshop.order.service.ShopBillPeriodService;
import com.legendshop.order.service.convert.ShopBillPeriodConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * 结算档期服务
 *
 * @author legendshop
 */
@Service
public class ShopBillPeriodServiceImpl implements ShopBillPeriodService {

	@Autowired
	private ShopBillPeriodDao shopBillPeriodDao;

	@Autowired
	private ShopBillPeriodConverter converter;

	@Override
	public ShopBillPeriodDTO getShopBillPeriod(Long id) {
		return converter.to(shopBillPeriodDao.getById(id));
	}

	@Override
	public int deleteShopBillPeriod(Long id) {
		return shopBillPeriodDao.deleteById(id);
	}

	@Override
	public Long saveShopBillPeriod(ShopBillPeriodDTO shopBillPeriodDTO) {
		if (ObjectUtil.isNotEmpty(shopBillPeriodDTO.getId())) {
			updateShopBillPeriod(shopBillPeriodDTO);
			return shopBillPeriodDTO.getId();
		}
		return shopBillPeriodDao.save(converter.from(shopBillPeriodDTO));
	}

	@Override
	public int updateShopBillPeriod(ShopBillPeriodDTO shopBillPeriodDTO) {
		return shopBillPeriodDao.update(converter.from(shopBillPeriodDTO));
	}

	@Override
	public long saveShopBillPeriodWithId(ShopBillPeriodDTO shopBillPeriodDTO, Long id) {
		shopBillPeriodDTO.setRecDate(DateUtil.date());
		return shopBillPeriodDao.save(converter.from(shopBillPeriodDTO), id);
	}

	@Override
	public Long getShopBillPeriodId() {
		return shopBillPeriodDao.createId();
	}

	@Override
	public String getShopBillMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		return sdf.format(c.getTime());
	}

	@Override
	public PageSupport<ShopBillPeriodDTO> queryShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery) {
		return converter.page(shopBillPeriodDao.getShopBillPeriod(shopBillPeriodQuery));
	}

	@Override
	public List<ShopBillPeriodExportDTO> exportShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery) {
		return shopBillPeriodDao.exportShopBillPeriod(shopBillPeriodQuery);
	}
}
