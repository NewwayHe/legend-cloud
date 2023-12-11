/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dto.ActivityCouponStatisticsDTO;
import com.legendshop.activity.query.ActivityCouponStatisticsQuery;
import com.legendshop.activity.service.ActivityCouponStatisticsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j

public class ActivityShopCouponServiceImpl implements ActivityCouponStatisticsService {

	private final CouponDao couponDao;

	@Override
	public PageSupport<ActivityCouponStatisticsDTO> pageActivityShopCoupon(ActivityCouponStatisticsQuery query) {
		return couponDao.pageActivityShopCoupon(query);
	}

	@Override
	public PageSupport<ActivityCouponStatisticsDTO> pageActivityPlatformCoupon(ActivityCouponStatisticsQuery query) {
		return couponDao.pageActivityPlatformCoupon(query);
	}

	@Override
	public List<ActivityCouponStatisticsDTO> getFlowExcelPlatform(ActivityCouponStatisticsQuery query) {
		return couponDao.getFlowExcelPlatform(query);
	}

	@Override
	public List<ActivityCouponStatisticsDTO> getFlowExcelShop(ActivityCouponStatisticsQuery query) {
		return couponDao.getFlowExcelShop(query);
	}

	/**
	 * 根据天数获取--前num天至昨天23点59分59秒的date数组。
	 *
	 * @param num 天数
	 * @return date[0]-开始时间  date[1]-结束时间
	 */
	private Date[] getDate(Integer num) {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);

		Date beginOfDay = DateUtil.beginOfDay(date);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] array = new Date[2];
		array[0] = startDate;
		array[1] = endDate;

		return array;
	}
}
