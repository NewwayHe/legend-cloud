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
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.QueryMap;
import cn.legendshop.jpaplus.support.SimpleSqlQuery;
import com.legendshop.activity.dao.CouponProductDao;
import com.legendshop.activity.entity.CouponProduct;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.activity.enums.CouponUserStatusEnum;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductItemDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券商品服务
 *
 * @author legendshop
 */
@Repository
public class CouponProductDaoImpl extends GenericDaoImpl<CouponProduct, Long> implements CouponProductDao {
	@Override
	public List<CouponProduct> queryByCouponId(Long couponId) {
		return this.queryByProperties(new EntityCriterion().eq("couponId", couponId));
	}


	@Override
	public PageSupport<SkuBO> queryCouponProductPage(CouponQuery couponQuery) {
		SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery(SkuBO.class, couponQuery.getPageSize(), couponQuery.getCurPage());
		QueryMap map = new QueryMap();
		map.put("shopId", couponQuery.getShopId());
		CouponUseTypeEnum useTypeEnum = CouponUseTypeEnum.fromCode(couponQuery.getUseType());
		switch (useTypeEnum) {
			case GENERAL: {
				simpleSqlQuery.setSqlAndParameter("CouponProduct.queryGeneral", map);
				break;
			}
			case INCLUDE: {
				map.put("couponId", couponQuery.getCouponId());
				simpleSqlQuery.setSqlAndParameter("CouponProduct.queryContains", map);
				break;
			}
			case EXCLUDE: {
				map.put("couponId", couponQuery.getCouponId());
				simpleSqlQuery.setSqlAndParameter("CouponProduct.queryExclusion", map);
				break;
			}
			default: {
				break;
			}
		}
		return querySimplePage(simpleSqlQuery);
	}

	@Override
	public Integer updateStatus(List<Long> ids, Integer status) {
		List<String> args = new ArrayList<>();
		args.add(status.toString());
		StringBuffer sql = new StringBuffer("update ls_coupon_user set status=? where id in (");
		for (int i = 0; i < ids.size(); i++) {
			sql.append("?,");
			args.add(ids.get(i).toString());
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString(), args.toArray());
	}

	@Override
	public Integer updateInvalidStatus(List<Long> ids) {
		List<String> args = new ArrayList<>();
		args.add(CouponUserStatusEnum.INVALID.getValue().toString());
		args.add(CouponUserStatusEnum.USED.getValue().toString());
		StringBuffer sql = new StringBuffer("update ls_coupon_user set status=? where status <> ? and id in (");
		for (int i = 0; i < ids.size(); i++) {
			sql.append("?,");
			args.add(ids.get(i).toString());
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString(), args.toArray());
	}

	@Override
	public Integer updateStatus(List<Long> ids, Integer status, Integer oldStatus) {
		List<String> args = new ArrayList<>();
		args.add(status.toString());
		args.add(oldStatus.toString());
		StringBuffer sql = new StringBuffer("update ls_coupon_user set status=? where status = ? and id in (");
		for (int i = 0; i < ids.size(); i++) {
			sql.append("?,");
			args.add(ids.get(i).toString());
		}
		sql.setLength(sql.length() - 1);
		sql.append(")");
		return update(sql.toString(), args.toArray());
	}

	@Override
	public List<ProductItemDTO> queryInfoByCouponId(Long couponId) {
		String sql = getSQL("CouponProduct.queryInfoByCouponId");
		return query(sql, ProductItemDTO.class, couponId);
	}

	@Override
	public List<CouponProduct> queryAllBySkuIds(Long couponId, List<Long> skuIds) {
		List<Object> args = new ArrayList<>();
		args.add(couponId);
		StringBuffer stringBuffer = new StringBuffer("select * From ls_coupon_product where coupon_id=? and sku_id in(");
		for (Long skuid : skuIds) {
			stringBuffer.append("?,");
			args.add(skuid);
		}
		stringBuffer.setLength(stringBuffer.length() - 1);
		stringBuffer.append(")");
		return query(stringBuffer.toString(), CouponProduct.class, args.toArray());
	}

	@Override
	public Integer deleteBySkuIds(Long couponId, List<Long> unSelectSkuId) {
		List<Object> args = new ArrayList<>();
		args.add(couponId);
		StringBuffer stringBuffer = new StringBuffer("delete from ls_coupon_product where coupon_id=? and sku_id in(");
		for (Long skuid : unSelectSkuId) {
			stringBuffer.append("?,");
			args.add(skuid);
		}
		stringBuffer.setLength(stringBuffer.length() - 1);
		stringBuffer.append(")");
		return update(stringBuffer.toString(), args.toArray());
	}

	@Override
	public List<Long> queryByCouponIdAndSkuList(Long couponId, List<Long> skuIdList) {
		List<Object> args = new ArrayList<>();
		args.add(couponId);
		StringBuffer stringBuffer = new StringBuffer("select sku_id from ls_coupon_product where coupon_id=? and sku_id in(");
		for (Long skuId : skuIdList) {
			stringBuffer.append("?,");
			args.add(skuId);
		}
		stringBuffer.setLength(stringBuffer.length() - 1);
		stringBuffer.append(")");
		return query(stringBuffer.toString(), Long.class, args.toArray());
	}

	@Override
	public void deleteByCouponId(Long couponId) {
		update("delete from ls_coupon_product where coupon_id=?", couponId);
	}
}
