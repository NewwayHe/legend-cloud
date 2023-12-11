/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.dto.AdvertiseStausCountDTO;
import com.legendshop.shop.query.AdvertiseQuery;

import java.util.List;

/**
 * (Advertise)表服务接口
 *
 * @author legendshop
 * @since 2022-04-27 15:23:36
 */
public interface AdvertiseService {

	/**
	 * 保存广告信息。
	 *
	 * @param advertiseDTO 广告数据传输对象
	 * @return 操作结果
	 */
	R saveAdvertise(AdvertiseDTO advertiseDTO);

	/**
	 * 更新广告信息。
	 *
	 * @param advertiseDTO 广告数据传输对象
	 * @return 操作结果
	 */
	R update(AdvertiseDTO advertiseDTO);

	/**
	 * 根据查询条件查询广告信息列表。
	 *
	 * @param query 广告查询条件
	 * @return 广告数据传输对象列表
	 */
	List<AdvertiseDTO> queryAdvertise(AdvertiseQuery query);

	/**
	 * 根据查询条件查询广告信息分页列表。
	 *
	 * @param query 广告查询条件
	 * @return 广告数据传输对象的分页支持对象
	 */
	PageSupport<AdvertiseDTO> queryAdvertisePage(AdvertiseQuery query);

	/**
	 * 根据查询条件查询广告数据报告分页列表。
	 *
	 * @param query 广告查询条件
	 * @return 广告统计数据传输对象的分页支持对象
	 */
	PageSupport<AdvertiseCountDTO> queryAdvertiseDataReport(AdvertiseQuery query);

	/**
	 * 更新广告统计时钟信息。
	 *
	 * @param advertiseCountDTO 广告统计数据传输对象
	 */
	void updateClock(AdvertiseCountDTO advertiseCountDTO);

	void updatePut(AdvertiseCountDTO advertiseCountDTO);

	/**
	 * 根据ID获取广告信息。
	 *
	 * @param id 广告ID
	 * @return 广告数据传输对象
	 */
	AdvertiseDTO getById(Long id);

	/**
	 * 根据ID删除广告信息。
	 *
	 * @param id 广告ID
	 * @return 删除操作结果
	 */
	Integer deleteById(Long id);

	/**
	 * 根据查询条件查询广告数量
	 *
	 * @param query 广告查询条件
	 * @return 广告数量传输对象列表
	 */
	List<AdvertiseStausCountDTO> queryadvertiseCount(AdvertiseQuery query);
}
