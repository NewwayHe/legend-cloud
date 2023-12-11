/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.*;
import com.legendshop.activity.bo.CouponShopBO;
import com.legendshop.activity.dao.CouponShopDao;
import com.legendshop.activity.entity.CouponShop;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponShopQuery;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 优惠券商家地址服务.
 *
 * @author legendshop
 */
@Repository
public class CouponShopDaoImpl extends GenericDaoImpl<CouponShop, Long> implements CouponShopDao {

	@Override
	public List<CouponShop> getCouponShopByCouponIds(List<Long> couponIds) {
		StringBuffer sql = new StringBuffer("select * from ls_coupon_shop where coupon_id in (");
		for (int i = 0; i < couponIds.size(); i++) {
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return this.query(sql.toString(), CouponShop.class, couponIds.toArray());
	}

	@Override
	public List<CouponShop> getCouponShopByCouponId(Long couponId) {
		return this.queryByProperties(new EntityCriterion().eq("couponId", couponId));
	}

	@Override
	public void deleteByCouponId(Long couponId) {
		update("delete from ls_coupon_shop where coupon_id=?", couponId);
	}

	@Override
	public Integer deleteByShopIdAndCouponId(Long couponShopId, Long couponId) {
		return update("delete from ls_coupon_shop where coupon_id = ? and shop_id = ?", couponId, couponShopId);
	}

	@Override
	public List<Long> queryShopByCouponId(Long couponId) {
		return query("SELECT shop_id FROM ls_coupon_shop WHERE coupon_id = ?", Long.class, couponId);
	}

	@Override
	public PageSupport<CouponShopBO> queryCouponShopPage(CouponQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(CouponShopBO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		CouponUseTypeEnum useTypeEnum = CouponUseTypeEnum.fromCode(couponQuery.getUseType());
		if (null != useTypeEnum) {
			switch (useTypeEnum) {
				case GENERAL: {
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryGeneral", map);
					break;
				}
				case INCLUDE: {
					map.put("couponId", couponQuery.getCouponId());
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryContains", map);
					break;
				}
				case EXCLUDE: {
					map.put("couponId", couponQuery.getCouponId());
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryExclusion", map);
					break;
				}
				default: {
					return new PageSupport<>(Collections.emptyList(), new DefaultPagerProvider(0L, couponQuery.getCurPage(), couponQuery.getPageSize()));
				}
			}
			return querySimplePage(simpleSqlQuery);
		}
		return new PageSupport<>(Collections.emptyList(), new DefaultPagerProvider(0L, couponQuery.getCurPage(), couponQuery.getPageSize()));
	}

	@Override
	public PageSupport<CouponShopBO> queryCouponShopPage(CouponShopQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(CouponShopBO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		CouponUseTypeEnum useTypeEnum = CouponUseTypeEnum.fromCode(couponQuery.getUseType());
		if (null != useTypeEnum) {
			switch (useTypeEnum) {
				case GENERAL: {
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryGeneralShop", map);
					break;
				}
				case INCLUDE: {
					map.put("couponId", couponQuery.getCouponId());
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryContainsShop", map);
					break;
				}
				case EXCLUDE: {
					map.put("couponId", couponQuery.getCouponId());
					simpleSqlQuery.setSqlAndParameter("CouponShop.queryExclusionShop", map);
					break;
				}
				default: {
					return new PageSupport<>(Collections.emptyList(), new DefaultPagerProvider(0L, couponQuery.getCurPage(), couponQuery.getPageSize()));
				}
			}
			return querySimplePage(simpleSqlQuery);
		}
		return new PageSupport<>(Collections.emptyList(), new DefaultPagerProvider(0L, couponQuery.getCurPage(), couponQuery.getPageSize()));
	}
}
