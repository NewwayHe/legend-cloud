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
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dao.ShippingActiveDao;
import com.legendshop.activity.entity.ShippingActive;
import com.legendshop.activity.enums.ShippingTypeEnum;
import com.legendshop.activity.query.ShippingActiveQuery;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 包邮活动Dao
 *
 * @author legendshop
 */
@Repository
public class ShippingActiveDaoImpl extends GenericDaoImpl<ShippingActive, Long> implements ShippingActiveDao {
	@Override
	public Long saveShippingActive(ShippingActive shippingActive) {
		shippingActive.setEndDate(DateUtil.endOfDay(DateUtil.date()));
		return save(shippingActive);
	}

	@Override
	public PageSupport<ShippingActive> queryShippingActiveList(ShippingActiveQuery query) {
		CriteriaQuery cq = new CriteriaQuery(ShippingActive.class, query.getPageSize(), query.getCurPage());
		cq.eq("shopId", query.getShopId());
		cq.like("name", query.getName(), MatchMode.ANYWHERE);
		cq.addDescOrder("createDate");
		return queryPage(cq);
	}

	@Override
	public ShippingActive getByIdAndShopId(Long activeId, Long shopId) {
		return getByProperties(new EntityCriterion().eq("shopId", shopId).eq("id", activeId));
	}

	@Override
	public List<ShippingActive> queryShopRuleEngine(Long shopId) {

		String now = getNowDate();

		String sql = "SELECT id,name,full_type AS fullType,reduce_type AS reduceType,full_value AS fullValue FROM ls_shipping_active WHERE shop_id = ? AND status = ? AND start_date <= ? AND end_date >= ?";
		List<ShippingActive> actives = query(sql, ShippingActive.class, shopId, ShippingTypeEnum.ONLINE.value(), now, now);

		if (CollUtil.isEmpty(actives)) {
			return null;
		}

		for (ShippingActive active : actives) {
			//商品类型活动
			if (active.getFullType().equals(ShippingTypeEnum.PROD.value())) {
				//查询参与的所有商品
				String queryProductId = "SELECT product_id FROM ls_shipping_product WHERE shipping_id = ? AND shop_id = ?";
				List<Long> productIds = query(queryProductId, Long.class, active.getId(), shopId);
				if (CollUtil.isNotEmpty(productIds)) {
					Long[] arrays = new Long[productIds.size()];
					for (int i = 0, j = productIds.size(); i < j; i++) {
						arrays[i] = productIds.get(i);
					}
					active.setProductIds(arrays);
				}
			}
		}

		return actives;
	}

	@Override
	public List<ShippingActive> queryOnLine(Long shopId, Long productId) {

		String now = getNowDate();

		String sql = "SELECT id,name,full_type AS fullType,reduce_type AS reduceType,full_value AS fullValue FROM ls_shipping_active WHERE shop_id = ? AND status = ? AND start_date <= ? "
				+ "AND end_date >= ? AND full_type = ? UNION SELECT a.id,a.name,a.full_type AS fullType,a.reduce_type AS reduceType,a.full_value AS fullValue "
				+ "FROM ls_shipping_active AS a INNER JOIN ls_shipping_product AS p ON a.id = p.shipping_id "
				+ "WHERE a.shop_id = ? AND a.status = ? AND a.start_date <= ? AND a.end_date >= ? AND a.full_type = ? AND p.product_id = ?";

		Object[] params = new Object[]{shopId, ShippingTypeEnum.ONLINE.value(), now, now, ShippingTypeEnum.SHOP.value(),
				shopId, ShippingTypeEnum.ONLINE.value(), now, now, ShippingTypeEnum.PROD.value(), productId};

		return query(sql, ShippingActive.class, params);
	}

	private String getNowDate() {
		Date nowDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(nowDate);
	}

}
