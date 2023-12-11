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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.SQLOperation;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dto.ActivityCouponStatisticsDTO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponItemExtDTO;
import com.legendshop.activity.entity.Coupon;
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.activity.query.ActivityCouponStatisticsQuery;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.vo.CouponVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优惠券(Coupon)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-10 10:52:37
 */
@Repository
@RequiredArgsConstructor
public class CouponDaoImpl extends GenericDaoImpl<Coupon, Long> implements CouponDao {

	@Override
	public List<Coupon> getByIds(List<Long> couponIds) {
		if (couponIds == null) {
			return null;
		}
		return queryByProperties(new LambdaEntityCriterion<Coupon>(Coupon.class).in(Coupon::getId, couponIds));
	}

	@Override
	public List<CouponItemExtDTO> queryByUserId(Long userId, Boolean shopProviderFlag, Integer status) {
		String sql = getSQL("Coupon.queryByUserId");
		return query(sql, CouponItemExtDTO.class, shopProviderFlag, userId, status);
	}

	@Override
	public List<CouponDTO> listReceivable(CouponQuery couponQuery) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		Date date = new Date();
		if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
			sql.append("with productList as(SELECT DISTINCT cp.coupon_id from ls_coupon_product cp where cp.sku_id in(");
			for (Long productId : couponQuery.getSkuIds()) {
				sql.append("?");
				sql.append(",");
				args.add(productId);
			}
			sql.setLength(sql.length() - 1);
			sql.append("))");
		}

		// 创建店铺临时表
		if (ObjectUtil.isNotEmpty(couponQuery.getShopId())) {
			if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
				sql.append(", ");
			} else {
				sql.append("with ");
			}
			sql.append(" shopList as(SELECT DISTINCT cs.coupon_id from ls_coupon_shop cs where cs.shop_id = ?)");
			args.add(couponQuery.getShopId());
		}
		sql.append(" select c.amount, c.count, c.id, c.min_point, c.pic, c.receive_count, c.receive_type, c.shop_id, c.title, c.use_count, c.use_day_later, c.integral, ");
		sql.append(" c.use_type,s.shop_name, c.designated_user, c.shop_provider_flag, c.create_time, lcu.status, lcu.use_start_time, lcu.use_end_time, c.receive_start_time, c.receive_end_time ");
		sql.append(" from ls_coupon c left join ls_shop_detail s on s.id=c.shop_id left join ls_coupon_user lcu on c.id = lcu.coupon_id where c.receive_type <> ? and c.receive_start_time<? and c.receive_end_time>? and c.status=? ");
		args.add(CouponReceiveTypeEnum.PWD.getValue());
		args.add(date);
		args.add(date);
		args.add(CouponStatusEnum.CONTAINS.getValue());
		if (ObjectUtil.isNotEmpty(couponQuery.getShopProviderFlag())) {
			sql.append(" and c.shop_provider_flag=? ");
			args.add(couponQuery.getShopProviderFlag());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getName())) {
			sql.append(" and c.title like CONCAT(CONCAT('%', ? ), '%')");
			args.add(couponQuery.getName());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getShopId())) {
			sql.append(" and (c.shop_id=? OR c.shop_id = 0) ");
			args.add(couponQuery.getShopId());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getReceiveType())) {
			sql.append(" and c.receive_type=? ");
			args.add(couponQuery.getReceiveType());
		}
		if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
			if (ObjectUtil.isEmpty(couponQuery.getShopId())) {
				sql.append("and ((c.use_type=? and c.id in(select * from productList)) or(c.use_type=? and c.id not in(select * from productList))or(c.use_type=?) )");
			} else {
				sql.append("and ((c.use_type=? and (c.id in(select * from productList) OR c.id IN (SELECT * FROM shopList))) or(c.use_type=? and (c.id not in(select * from productList) AND c.id NOT IN (SELECT * FROM shopList)))or(c.use_type=?) )");
			}
			args.add(CouponUseTypeEnum.INCLUDE.getValue());
			args.add(CouponUseTypeEnum.EXCLUDE.getValue());
			args.add(CouponUseTypeEnum.GENERAL.getValue());
		} else {
			if (ObjectUtil.isNotEmpty(couponQuery.getShopId()) && (ObjectUtil.isEmpty(couponQuery.getShopProviderFlag()) || !couponQuery.getShopProviderFlag())) {
				sql.append("and ((c.use_type=? and c.id in(select * from shopList)) or(c.use_type=? and c.id not in(select * from shopList))or(c.use_type=?) )");
				args.add(CouponUseTypeEnum.INCLUDE.getValue());
				args.add(CouponUseTypeEnum.EXCLUDE.getValue());
				args.add(CouponUseTypeEnum.GENERAL.getValue());
			}
		}
		sql.append(" group by c.id ");
		return query(sql.toString(), CouponDTO.class, args.toArray());
	}

	@Override
	public PageSupport<CouponDTO> queryReceivablePage(CouponQuery couponQuery) {
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<>();
		Date date = new Date();
		if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
			sql.append("with productList as(SELECT DISTINCT cp.coupon_id from ls_coupon_product cp where cp.sku_id in(");
			for (Long productId : couponQuery.getSkuIds()) {
				sql.append("?");
				sql.append(",");
				args.add(productId);
			}
			sql.setLength(sql.length() - 1);
			sql.append("))");
		}

		// 创建店铺临时表
		if (ObjectUtil.isNotEmpty(couponQuery.getShopId())) {
			if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
				sql.append(", ");
			} else {
				sql.append("with ");
			}
			sql.append(" shopList as(SELECT DISTINCT cs.coupon_id from ls_coupon_shop cs where cs.shop_id = ?)");
			args.add(couponQuery.getShopId());
		}
		sql.append(" select c.amount, c.count, c.id, c.min_point, c.pic, c.receive_count, c.receive_type, c.shop_id, c.title, c.use_count, c.use_day_later, c.integral, ");
		sql.append(" c.use_type,s.shop_name, c.designated_user, c.shop_provider_flag, c.create_time, lcu.status, lcu.use_start_time, lcu.use_end_time, c.receive_start_time, c.receive_end_time ");
		sql.append(" from ls_coupon c left join ls_shop_detail s on s.id=c.shop_id left join ls_coupon_user lcu on c.id = lcu.coupon_id where c.receive_type <> ? and c.receive_start_time<? and c.receive_end_time>? and c.status=? ");
		args.add(CouponReceiveTypeEnum.PWD.getValue());
		args.add(date);
		args.add(date);
		args.add(CouponStatusEnum.CONTAINS.getValue());
		if (ObjectUtil.isNotEmpty(couponQuery.getShopProviderFlag())) {
			sql.append(" and c.shop_provider_flag=? ");
			args.add(couponQuery.getShopProviderFlag());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getName())) {
			sql.append(" and c.title like CONCAT(CONCAT('%', ? ), '%')");
			args.add(couponQuery.getName());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getShopId())) {
			sql.append(" and (c.shop_id=? OR c.shop_id = 0) ");
			args.add(couponQuery.getShopId());
		}
		if (ObjectUtil.isNotEmpty(couponQuery.getReceiveType())) {
			sql.append(" and c.receive_type=? ");
			args.add(couponQuery.getReceiveType());
		}
		if (CollUtil.isNotEmpty(couponQuery.getSkuIds())) {
			if (ObjectUtil.isEmpty(couponQuery.getShopId())) {
				sql.append("and ((c.use_type=? and c.id in(select * from productList)) or(c.use_type=? and c.id not in(select * from productList))or(c.use_type=?) )");
			} else {
				sql.append("and ((c.use_type=? and (c.id in(select * from productList) OR c.id IN (SELECT * FROM shopList))) or(c.use_type=? and (c.id not in(select * from productList) AND c.id NOT IN (SELECT * FROM shopList)))or(c.use_type=?) )");
			}
			args.add(CouponUseTypeEnum.INCLUDE.getValue());
			args.add(CouponUseTypeEnum.EXCLUDE.getValue());
			args.add(CouponUseTypeEnum.GENERAL.getValue());
		} else {
			if (ObjectUtil.isNotEmpty(couponQuery.getShopId()) && (ObjectUtil.isEmpty(couponQuery.getShopProviderFlag()) || !couponQuery.getShopProviderFlag())) {
				sql.append("and ((c.use_type=? and c.id in(select * from shopList)) or(c.use_type=? and c.id not in(select * from shopList))or(c.use_type=?) )");
				args.add(CouponUseTypeEnum.INCLUDE.getValue());
				args.add(CouponUseTypeEnum.EXCLUDE.getValue());
				args.add(CouponUseTypeEnum.GENERAL.getValue());
			}
		}
		sql.append(" group by c.id ");
		List<CouponDTO> list = query(sql.toString(), CouponDTO.class, args.toArray());
		PageSupport<CouponDTO> page = new PageSupport<>();
		page.setTotal(list.size());
		sql.append(" limit " + (couponQuery.getCurPage() - 1) * couponQuery.getPageSize() + "," + couponQuery.getPageSize());
		List<CouponDTO> pageList = query(sql.toString(), CouponDTO.class, args.toArray());
		page.setResultList(pageList);

		page.setCurPageNO(couponQuery.getCurPage());
		page.setPageSize(couponQuery.getPageSize());
		page.setPageCount((list.size() % page.getPageSize() == 0 ? list.size() / page.getPageSize() : list.size() / page.getPageSize() + 1));
		return page;
	}

	@Override
	public List<Coupon> getOffLineId() {
		EntityCriterion entityCriterion = new EntityCriterion();
		List<Integer> arrayList = new ArrayList<>();
		arrayList.add(CouponStatusEnum.NOT_STARTED.getValue());
		arrayList.add(CouponStatusEnum.CONTAINS.getValue());
		arrayList.add(CouponStatusEnum.PAUSE.getValue());
		entityCriterion.in("status", arrayList).le("receiveEndTime", new Date());
		return queryByProperties(entityCriterion);
	}

	@Override
	public List<Coupon> queryPreOnline() {
		EntityCriterion entityCriterion = new EntityCriterion();
		Date date = new Date();
		entityCriterion.le("receiveStartTime", date)
				.gt("receiveEndTime", date)
				.eq("status", CouponStatusEnum.NOT_STARTED);
		return queryByProperties(entityCriterion);
	}

	@Override
	public void updateReceiveNum(Long couponId) {
		update("update ls_coupon set receive_count=receive_count+1 WHERE id =? ", couponId);
	}

	@Override
	public PageSupport<CouponVO> queryCouponPage(CouponQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(CouponVO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", couponQuery.getShopId());
		map.put("shopProviderFlag", couponQuery.getShopProviderFlag());
		map.like("shopName", couponQuery.getShopName(), MatchMode.ANYWHERE);
		map.like("name", couponQuery.getName(), MatchMode.ANYWHERE);
		map.put("receiveTimeBeg", couponQuery.getReceiveTimeBeg());
		map.put("receiveTimeEnd", couponQuery.getReceiveTimeEnd());
		map.put("useTimeBeg", couponQuery.getUseTimeBeg());
		map.put("useTimeEnd", couponQuery.getUseTimeEnd());
		map.put("receiveType", couponQuery.getReceiveType());
		if (ObjectUtil.isNotEmpty(couponQuery.getUseType())) {
			if (CouponUseTypeEnum.GENERAL.getValue().equals(couponQuery.getUseType())) {
				map.put("useType", "and c.use_type = 0");
			} else {
				map.put("useType", "and c.use_type <> 0");
			}
		}

		// candy确认：平台的删除状态与商家的删除状态互不影响, 平台的删除状态只是不显示在列表上
		if (ObjectUtil.isNotEmpty(couponQuery.getIsPlatform())) {
			if (couponQuery.getIsPlatform()) {
				if (ObjectUtil.isNotEmpty(couponQuery.getShopId()) && 0L == couponQuery.getShopId()) {
					map.put("deleteStatus", " and c.platform_delete_status = 0 and c.status <> -2");
				} else {
					map.put("deleteStatus", " and c.platform_delete_status = 0");
				}
			} else {
				map.put("deleteStatus", " and c.status <> -2");
			}
		}
		map.put("status", couponQuery.getStatus());
		simpleSqlQuery.setSqlAndParameter("Coupon.queryCouponPage", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public Integer updateStatus(Long id, Integer status) {
		return update("update ls_coupon set status=? where id =?", status, id);
	}

	@Override
	public Integer batchUpdateStatus(List<Long> ids, Integer status) {
		List<Long> args = new ArrayList<>();
		args.add(status.longValue());
		StringBuffer sb = new StringBuffer("update ls_coupon set status=? where id in( ");
		for (Long id : ids) {
			sb.append("?,");
			args.add(id);
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		return update(sb.toString(), args.toArray());
	}

	@Override
	public Coupon getCouponByUserCouponIdAndShopId(Long userCouponId, Long shopId) {
		QueryMap queryMap = new QueryMap();
		queryMap.put("userCouponId", userCouponId);
		queryMap.put("shopId", shopId);

		SQLOperation operation = getSQLAndParams("Coupon.getCouponByUserCouponIdAndShopId", queryMap);
		return get(operation.getSql(), Coupon.class, operation.getParams());
	}

	@Override
	public PageSupport<CouponDTO> queryCoupon(CouponQuery couponQuery) {
		QueryMap map = new QueryMap();
		map.put("couponId", couponQuery.getCouponId());
		map.put("shopProviderFlag", couponQuery.getShopProviderFlag());
		map.put("userId", couponQuery.getUserId());
		map.put("shopId", couponQuery.getShopId());
		map.in("skuIds", couponQuery.getSkuIds());
		map.put("name", couponQuery.getName());
		map.put("receiveType", couponQuery.getReceiveType());
		map.put("useType", couponQuery.getUseType());
		map.put("status", couponQuery.getStatus());
		map.put("shopName", couponQuery.getShopName());
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(CouponDTO.class, couponQuery);
		simpleSqlQuery.setSqlAndParameter("Coupon.queryCoupon", map);
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public List<Long> queryCouponSkuIds(Long couponId) {
		return query("SELECT sku_id  FROM ls_coupon_product WHERE coupon_id=?", Long.class, couponId);
	}

	@Override
	public List<Long> queryCouponShopIds(Long couponId) {
		return query("SELECT shop_id  FROM ls_coupon_shop WHERE coupon_id=?", Long.class, couponId);
	}

	/**
	 *	活动类 列表 商家优惠卷
	 */
	@Override
	public PageSupport<ActivityCouponStatisticsDTO> pageActivityShopCoupon(ActivityCouponStatisticsQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(ActivityCouponStatisticsDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();

		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			map.put("orderBy", " order by " + StrUtil.toUnderlineCase(query.getProp()) + " " + query.getOrder());
		}

		map.put("shopProviderFlag", query.getShopProviderFlag());
		//搜索 领券时间
		map.put("receiveStartTime", query.getReceiveStartTime());
		map.put("receiveEndTime", query.getReceiveEndTime());
		simpleSqlQuery.setSqlAndParameter("Coupon.pageActivityShopCoupon", map);
		PageSupport<ActivityCouponStatisticsDTO> pageSupport = querySimplePage(simpleSqlQuery);
		return pageSupport;
	}

	/**
	 *	活动类 列表 平台优惠卷
	 */
	@Override
	public PageSupport<ActivityCouponStatisticsDTO> pageActivityPlatformCoupon(ActivityCouponStatisticsQuery query) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(ActivityCouponStatisticsDTO.class, query.getPageSize(), query.getCurPage());
		QueryMap map = new QueryMap();
		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			map.put("orderBy", " order by " + StrUtil.toUnderlineCase(query.getProp()) + " " + query.getOrder());
		}
		map.put("shopProviderFlag", query.getShopProviderFlag());
		//搜索 领券时间
		map.put("receiveStartTime", query.getReceiveStartTime());
		map.put("receiveEndTime", query.getReceiveEndTime());

		simpleSqlQuery.setSqlAndParameter("Coupon.pageActivityPlatformCoupon", map);
		PageSupport<ActivityCouponStatisticsDTO> pageSupport = querySimplePage(simpleSqlQuery);
		return pageSupport;
	}

	@Override
	public Integer updateUseCount(Long id, Long useCount) {
		return update("update ls_coupon set use_count = use_count + ?, update_time = ? where id = ? ", useCount, DateUtil.date(), id);
	}

	@Override
	public List<ActivityCouponStatisticsDTO> getFlowExcelPlatform(ActivityCouponStatisticsQuery query) {
		QueryMap map = new QueryMap();
		map.put("shopProviderFlag", query.getShopProviderFlag());
		//搜索 领券时间

		map.put("receiveStartTime", query.getReceiveStartTime());
		map.put("receiveEndTime", query.getReceiveEndTime());

		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			map.put("orderBy", " order by " + StrUtil.toUnderlineCase(query.getProp()) + " " + query.getOrder());
		}

		SQLOperation operation = this.getSQLAndParams("Coupon.getFlowExcelPlatform", map);
		return query(operation.getSql(), ActivityCouponStatisticsDTO.class, operation.getParams());
	}

	@Override
	public List<ActivityCouponStatisticsDTO> getFlowExcelShop(ActivityCouponStatisticsQuery query) {
		QueryMap map = new QueryMap();

		map.put("shopProviderFlag", query.getShopProviderFlag());
		//搜索 领券时间
		map.put("receiveStartTime", query.getReceiveStartTime());
		map.put("receiveEndTime", query.getReceiveEndTime());

		if (StrUtil.isNotEmpty(query.getProp()) && StrUtil.isNotEmpty(query.getOrder())) {
			map.put("orderBy", " order by " + StrUtil.toUnderlineCase(query.getProp()) + " " + query.getOrder());
		}
		SQLOperation operation = this.getSQLAndParams("Coupon.getFlowExcelShop", map);
		return this.query(operation.getSql(), ActivityCouponStatisticsDTO.class, operation.getParams());
	}
}
