/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao.impl;

import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.QueryMap;
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.pay.dao.YeepayLocationDao;
import com.legendshop.pay.entity.YeepayLocation;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 易宝支付地区编码表(YeepayLocation)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-04-08 14:28:08
 */
@Repository
public class YeepayLocationDaoImpl extends GenericDaoImpl<YeepayLocation, Long> implements YeepayLocationDao {

	@Override
	public List<KeyValueEntityDTO> loadProvince() {
		String sql = this.getSQL("YeepayLocation.loadProvince");

		return query(sql, (rs, i) -> {
			KeyValueEntityDTO keyValueEntityDTO = new KeyValueEntityDTO();
			keyValueEntityDTO.setKey(rs.getString("province_code"));
			keyValueEntityDTO.setValue(rs.getString("province_name"));
			return keyValueEntityDTO;
		});
	}

	@Override
	public List<KeyValueEntityDTO> loadCity(String provinceCode) {

		if (provinceCode == null) {
			return Collections.emptyList();
		}

		QueryMap queryMap = new QueryMap();
		queryMap.put("provinceCode", provinceCode);

		SQLOperation operation = this.getSQLAndParams("YeepayLocation.loadCity", queryMap);

		return this.query(operation.getSql(), operation.getParams(), ((rs, i) -> {
			KeyValueEntityDTO keyValueEntityDTO = new KeyValueEntityDTO();
			keyValueEntityDTO.setKey(rs.getString("city_code"));
			keyValueEntityDTO.setValue(rs.getString("city_name"));
			return keyValueEntityDTO;
		}));
	}

	@Override
	public List<KeyValueEntityDTO> loadArea(String cityCode) {

		if (cityCode == null) {
			return Collections.emptyList();
		}

		QueryMap queryMap = new QueryMap();
		queryMap.put("cityCode", cityCode);

		SQLOperation operation = this.getSQLAndParams("YeepayLocation.loadArea", queryMap);

		return this.query(operation.getSql(), operation.getParams(), ((rs, i) -> {
			KeyValueEntityDTO keyValueEntityDTO = new KeyValueEntityDTO();
			keyValueEntityDTO.setKey(rs.getString("area_code"));
			keyValueEntityDTO.setValue(rs.getString("area_name"));
			return keyValueEntityDTO;
		}));
	}

}
