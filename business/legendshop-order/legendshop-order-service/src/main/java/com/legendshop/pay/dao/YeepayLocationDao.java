/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.common.core.dto.KeyValueEntityDTO;
import com.legendshop.pay.entity.YeepayLocation;

import java.util.List;

/**
 * 易宝支付地区编码表(YeepayLocation)表数据库访问层
 *
 * @author legendshop
 * @since 2021-04-08 14:28:07
 */
public interface YeepayLocationDao extends GenericDao<YeepayLocation, Long> {

	/**
	 * 加载省份
	 *
	 * @return
	 */
	List<KeyValueEntityDTO> loadProvince();


	/**
	 * 加载城市
	 *
	 * @param provinceCode
	 * @return
	 */
	List<KeyValueEntityDTO> loadCity(String provinceCode);


	/**
	 * 加载地区
	 *
	 * @param cityCode
	 * @return
	 */
	List<KeyValueEntityDTO> loadArea(String cityCode);

}
