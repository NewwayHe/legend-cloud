/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.data.dao.CouponViewDao;
import com.legendshop.data.dto.CouponViewDTO;
import com.legendshop.data.entity.CouponView;
import com.legendshop.data.service.CouponViewService;
import com.legendshop.data.service.convert.CouponViewConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * (CouponView)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-06 11:49:51
 */
@Slf4j
@Service
public class CouponViewServiceImpl extends BaseServiceImpl<CouponViewDTO, CouponViewDao, CouponViewConverter> implements CouponViewService {

	@Autowired
	private CouponViewDao couponViewDao;
	@Autowired
	private CouponViewConverter couponViewConverter;
	@Autowired
	private RedisTemplate redisTemplate;


	@Override
	public void updateVisit(CouponViewDTO coupon) {
		Date createTime = DateUtil.beginOfDay(coupon.getCreateTime());
		Long couponUserId = coupon.getCouponId();
		String source = coupon.getSource();
		Long userId = coupon.getUserId();

		CouponView couponView = couponViewDao.queryVisit(couponUserId, source, createTime);

		if (couponView == null) {
			couponView = new CouponView();
			coupon.setViewPeople(0);
			coupon.setViewFrequency(0);
			coupon.setCreateTime(createTime);
			Long id = couponViewDao.save(couponViewConverter.from(coupon));
			coupon.setId(id);
			BeanUtil.copyProperties(coupon, couponView);
		}

		long between = DateUtil.between(DateUtil.date(), DateUtil.beginOfDay(DateUtil.offsetDay(createTime, 1)), DateUnit.SECOND, true);
		log.info("离当天结束还有 {} 秒", between);
		//数据库有该商品记录，根据来源更新浏览信息
		switch (source) {
			case "MP":
				//根据用户id+source+商品id查询是否浏览过
				//redis没有用户该端记录，人数+1
				if (!redisTemplate.hasKey(userId + "MP" + couponUserId)) {
					couponView.setViewPeople(couponView.getViewPeople() + 1);
					//保存浏览用户到redis
					redisTemplate.opsForValue().set(userId + "MP" + couponUserId, 1, between, TimeUnit.SECONDS);
				}
				break;
			case "MINI":
				if (!redisTemplate.hasKey(userId + "MINI" + couponUserId)) {
					couponView.setViewPeople(couponView.getViewPeople() + 1);
					redisTemplate.opsForValue().set(userId + "MINI" + couponUserId, userId + "MINI" + couponUserId, between, TimeUnit.SECONDS);
				}
				break;
			case "APP":
				if (!redisTemplate.hasKey(userId + "App" + couponUserId)) {
					couponView.setViewPeople(couponView.getViewPeople() + 1);
					redisTemplate.opsForValue().set(userId + "App" + couponUserId, userId + "App" + couponUserId, between, TimeUnit.SECONDS);
				}
				break;
			case "H5":
				if (!redisTemplate.hasKey(userId + "H5" + couponUserId)) {
					couponView.setViewPeople(couponView.getViewPeople() + 1);
					redisTemplate.opsForValue().set(userId + "H5" + couponUserId, userId + "H5" + couponUserId, between, TimeUnit.SECONDS);

				}
				break;
			default:

		}

		couponView.setUpdateTime(DateUtil.date());
		couponView.setViewFrequency(couponView.getViewFrequency() + 1);
		couponViewDao.updateProperties(couponView);
	}
}





