/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dao.CouponShopDao;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponShopDTO;
import com.legendshop.activity.entity.CouponShop;
import com.legendshop.activity.mq.producer.CouponProducerService;
import com.legendshop.activity.service.CouponShopService;
import com.legendshop.activity.service.convert.CouponShopConverter;
import com.legendshop.common.core.constant.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 红包 店铺关联服务
 *
 * @author legendshop
 */
@Service
public class CouponShopServiceImpl implements CouponShopService {

	private static final String getShopNamesByCouponIdSql = "SELECT s.shop_name AS shopName FROM ls_coupon_shop cs , ls_shop_detail s WHERE cs.shop_id=s.shop_id AND cs.coupon_id=?";

	@Autowired
	private CouponDao couponDao;

	@Autowired
	private CouponShopDao couponShopDao;

	@Autowired
	private CouponShopConverter converter;

	@Autowired
	private CouponProducerService couponProducerService;

	@Override
	public List<Long> saveCouponShops(List<CouponShopDTO> couponShops) {
		return couponShopDao.save(converter.from(couponShops));
	}

	@Override
	public List<CouponShopDTO> getCouponShopByCouponId(Long couponId) {
		return converter.to(couponShopDao.queryByProperties(new EntityCriterion().eq("couponId", couponId)));
	}

	public CouponShopDao getCouponShopDao() {
		return couponShopDao;
	}

	@Override
	public List<String> getShopNamesByCouponId(Long id) {
		return this.couponShopDao.query(getShopNamesByCouponIdSql, String.class, id);
	}

	@Override
	public List<CouponShopDTO> getCouponShopByCouponIds(List<Long> couponIds) {
		return converter.to(couponShopDao.getCouponShopByCouponIds(couponIds));
	}

	@Override
	public R update(CouponDTO couponDTO) {
		List<Long> selectShopId = couponDTO.getSelectShopId();
		if (CollUtil.isEmpty(selectShopId)) {
			return R.ok();
		}
		Long couponId = couponDTO.getId();
		/*判断有无优惠券id，没有则生成一个*/
		if (ObjectUtil.isEmpty(couponId)) {
			couponId = couponDao.createId();
			/*发送延时队列，如果15min不保存优惠券，就删除优惠券店铺*/
			couponProducerService.deleteCouponShop(couponId);
		}
		// 删除重建
		couponShopDao.deleteByCouponId(couponId);
		List<CouponShop> couponShopList = new ArrayList<>();
		//数据库没有直接新增
		if (CollUtil.isNotEmpty(selectShopId)) {
			for (Long shopId : selectShopId) {
				CouponShop couponShop = new CouponShop();
				couponShop.setCouponId(couponId);
				couponShop.setShopId(shopId);
				couponShopList.add(couponShop);
			}
			couponShopDao.save(couponShopList);
		}
		return R.ok(couponId);
	}

	@Override
	public Integer delete(Long couponShopId, Long couponId) {
		return couponShopDao.deleteByShopIdAndCouponId(couponShopId, couponId);
	}

	@Override
	public R copyCouponShop(Long oldCouponId) {
		Long couponId = couponDao.createId();
		List<CouponShop> couponShopList = couponShopDao.getCouponShopByCouponId(oldCouponId);
		if (CollUtil.isNotEmpty(couponShopList)) {
			for (CouponShop couponShop : couponShopList) {
				couponShop.setId(null);
				couponShop.setCouponId(couponId);
			}
			couponShopDao.save(couponShopList);
		}
		return R.ok(couponId);
	}

	@Override
	public List<Long> queryShopByCouponId(Long couponId) {
		return couponShopDao.queryShopByCouponId(couponId);
	}

}
