/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.bo.MyCouponBO;
import com.legendshop.activity.dao.CouponUserDao;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponUserQuery;
import com.legendshop.product.bo.SkuBO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class CouponUserDaoImpl extends GenericDaoImpl<CouponUser, Long> implements CouponUserDao {
	@Override
	public List<CouponUser> queryByUserId(Long userId) {
		if (userId == null) {
			return Collections.emptyList();
		}
		return this.queryByProperties(new EntityCriterion().eq("userId", userId).eq("status", CouponUserStatusEnum.UNUSED.getValue()));
	}

	@Override
	public PageSupport<MyCouponBO> queryMyCouponPage(CouponQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(MyCouponBO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("userId", couponQuery.getUserId());
		map.put("shopId", couponQuery.getShopId());
		map.like("name", couponQuery.getName());
		map.like("shopName", couponQuery.getShopName());
		map.put("shopProviderFlag", couponQuery.getShopProviderFlag());
		if (CouponStatusEnum.CONTAINS.getValue().equals(couponQuery.getStatus())) {
			map.put("statusFlag", " AND (cu.status = 0 OR cu.status = 1) ");
		} else {
			map.put("status", couponQuery.getStatus());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getShopProviderFlag())) {
			map.put("receiveType", " and c.receive_type <> " + CouponReceiveTypeEnum.INTEGRAL.getValue());
			if (CouponReceiveTypeEnum.INTEGRAL.getValue().equals(couponQuery.getReceiveType())) {
				map.put("receiveType", " and c.receive_type = " + CouponReceiveTypeEnum.INTEGRAL.getValue());
			}
		}
		simpleSqlQuery.setSqlAndParameter("CouponUser.queryMyCouponPage", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public PageSupport<CouponUserBO> queryPage(CouponQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(CouponUserBO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("couponId", couponQuery.getCouponId());
		simpleSqlQuery.setSqlAndParameter("CouponUser.queryPage", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public Integer updateUserIdByPwd(Long id, Long userId, Date onTime, Date offTime) {
		return update("update ls_coupon_user set user_id=?,use_start_time=?,use_end_time=?,get_time=NOW() where ISNULL(user_id) and id=?", userId, onTime, offTime, id);
	}

	@Override
	public PageSupport<SkuBO> querySkuPageById(CouponUserQuery couponUserQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(SkuBO.class, couponUserQuery.getPageSize(), couponUserQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("couponId", couponUserQuery.getCouponId());
		simpleSqlQuery.setSqlAndParameter("CouponUser.querySkuPageById", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public List<CouponUserDTO> queryUnusedCouponIdByUserId(Long userId) {
		List<String> args = new ArrayList<>();
		args.add(userId.toString());
		args.add(CouponUserStatusEnum.UNUSED.getValue().toString());
		args.add(CouponUserStatusEnum.NOT_STARTED.getValue().toString());
		return query(getSQL("CouponUser.queryUnusedCouponIdByUserId"), CouponUserDTO.class, args.toArray());
	}

	@Override
	public Integer userCouponValid() {
		Date date = new Date();
		return update("update ls_coupon_user cu set status=? where cu.status=? and cu.use_start_time<=? and cu.use_end_time>?"
				, CouponUserStatusEnum.UNUSED.getValue(), CouponUserStatusEnum.NOT_STARTED.getValue(), date, date);
	}

	@Override
	public Integer userCouponInvalid() {
		return update("update ls_coupon_user cu set status=? where cu.status in(?,?) and cu.use_end_time<=?"
				, CouponUserStatusEnum.INVALID.getValue(), CouponUserStatusEnum.UNUSED.getValue(), CouponUserStatusEnum.NOT_STARTED.getValue(), new Date());
	}

	@Override
	public CouponUser getByPwd(String pwd) {
		if (StrUtil.isBlank(pwd)) {
			return null;
		}
		return getByProperties(new EntityCriterion().eq("password", pwd).isNull("userId"));
	}

	@Override
	public List<CouponUser> queryByUserIdAndCouponId(Long userId, Long couponId) {
		if (userId == null || couponId == null) {
			return Collections.emptyList();
		}
		EntityCriterion entityCriterion = new EntityCriterion();
		entityCriterion.eq("userId", userId).eq("couponId", couponId);
		return queryByProperties(entityCriterion);
	}

	@Override
	public Integer updateStatus(Long id, Integer status) {
		return update("update ls_coupon_user set status=? where id=? and status = ?", status, id, CouponUserStatusEnum.UNUSED.getValue());
	}

	@Override
	public Integer updateUserCouponStatus(Long couponId) {
		return update("update ls_coupon_user set status=? where coupon_id=? ", CouponUserStatusEnum.INVALID.getValue(), couponId);
	}

	@Override
	public List<CouponUser> getByOrderNumber(String orderNumber) {
		return queryByProperties(new EntityCriterion().like("orderNumber", orderNumber, MatchMode.ANYWHERE));
	}

	@Override
	public Integer userCouponCount(Long userId) {
		return get("SELECT COUNT(*) FROM ls_coupon_user WHERE user_id = ? AND status = 1", Integer.class, userId);
	}

	@Override
	public void batchUpdateStatus(Long userId, List<Long> ids, String orderNumber, Integer status) {
		if (CollUtil.isEmpty(ids)) {
			return;
		}
		List<Object> args = new ArrayList<>();
		args.add(status.longValue());
		args.add(orderNumber);
		args.add(userId);
		StringBuilder sb = new StringBuilder("update ls_coupon_user set status=?,order_number=?,use_time=now() where user_id=? and id in( ");
		for (Long id : ids) {
			sb.append("?,");
			args.add(id);
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		update(sb.toString(), args.toArray());
	}

	@Override
	public Integer getAvailableCount(Long userId) {
		return get("select count(*) from ls_coupon_user where status = 1 and user_id = ?", Integer.class, userId);
	}

	@Override
	public Integer getUsedCount(Long userId) {
		return get("select count(*) from ls_coupon_user where status = 2 and user_id = ?", Integer.class, userId);
	}

	@Override
	public Integer getExpireCount(Long userId) {
		return get("select count(*) from ls_coupon_user where status = -1 and user_id = ?", Integer.class, userId);
	}

	@Override
	public void batchUpdateStatusById(List<Long> couponUserId, Integer status) {
		List<Object> args = new ArrayList<>();
		args.add(status.longValue());

		StringBuilder sb = new StringBuilder("update ls_coupon_user set status=?,order_number=?,use_time=now() where user_id=? and id in( ");
		for (Long id : couponUserId) {
			sb.append("?,");
			args.add(id);
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		update(sb.toString(), args.toArray());
	}

}
