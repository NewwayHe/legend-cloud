/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.dto.AdvertiseStausCountDTO;
import com.legendshop.shop.entity.Advertise;
import com.legendshop.shop.query.AdvertiseQuery;

import java.util.List;

/**
 * (Advertise)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-27 15:23:36
 */
public interface AdvertiseDao extends GenericDao<Advertise, Long> {

	List<Advertise> queryAdvertise(AdvertiseQuery query);

	List<AdvertiseStausCountDTO> queryAdvertiseAll();

	PageSupport<AdvertiseDTO> queryAdvertisePage(AdvertiseQuery query);

	PageSupport<AdvertiseCountDTO> queryAdvertiseDataReport(AdvertiseQuery query);
}
