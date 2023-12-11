/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import cn.legendshop.jpaplus.util.StringUtils;
import com.legendshop.order.dao.ShopOrderBillDao;
import com.legendshop.order.dto.ShopOrderBillDTO;
import com.legendshop.order.entity.ShopOrderBill;
import com.legendshop.order.enums.ShopOrderBillStatusEnum;
import com.legendshop.order.excel.ShopOrderBillExportDTO;
import com.legendshop.order.query.ShopOrderBillQuery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 商家订单结算表Dao
 *
 * @author legendshop
 */
@Repository
public class ShopOrderBillDaoImpl extends GenericDaoImpl<ShopOrderBill, Long> implements ShopOrderBillDao {

	@Override
	public ShopOrderBill getLastShopOrderBillByShopId(Long shopId) {
		List<ShopOrderBill> shopOrderBills = this.queryByProperties(new EntityCriterion().eq("shopId", shopId).addDescOrder("endDate"));
		if (CollUtil.isNotEmpty(shopOrderBills)) {
			return shopOrderBills.get(0);
		} else {
			return null;
		}
	}

	@Override
	public PageSupport<ShopOrderBill> getShopOrderBillPage(ShopOrderBillQuery query) {
		CriteriaQuery cq = new CriteriaQuery(ShopOrderBill.class, query.getPageSize(), query.getCurPage());
		cq.like("shopName", query.getShopName(), MatchMode.ANYWHERE);
		cq.like("flag", query.getFlag(), MatchMode.ANYWHERE);
		cq.eq("shopId", query.getShopId());
		cq.like("sn", query.getSn(), MatchMode.ANYWHERE);
		cq.eq("status", query.getStatus());
		if (StringUtils.isNotBlank(query.getStartTime())) {
			cq.gt("createDate", DateUtil.beginOfDay(DateUtil.parse(query.getStartTime())));
			cq.lt("createDate", DateUtil.endOfDay(DateUtil.parse(query.getStartTime())));
		}
		cq.addDescOrder("createDate");
		return queryPage(cq);
	}

	/**
	 * 检查是否有存在的结算单
	 */
	@Override
	public boolean getShopOrderBill(Long shopId, Date startDate, Date endDate) {
		String sql = "select count(1) from ls_shop_order_bill where shop_id = ? and start_date = ? and end_date = ?";
		Integer result = get(sql, Integer.class, shopId, startDate, endDate);
		return result > 0;
	}


	@Override
	public int payBill(Integer status, Long id, String payDate, String payContent) {
		String sql = "update ls_shop_order_bill set status = ?, pay_date = ?, pay_content = ? where id = ?";
		return update(sql, status, DateUtil.parseDate(payDate), payContent, id);
	}

	@Override
	@Cacheable(value = "shopOrderBill", key = "#id")
	public ShopOrderBill getShopOrderBillById(Long id) {
		return getById(id);
	}

	@Override
	public List<ShopOrderBillExportDTO> exportShopBillPeriod(ShopOrderBillQuery query) {
		LambdaEntityCriterion<ShopOrderBillExportDTO> criterion = new LambdaEntityCriterion<>(ShopOrderBillExportDTO.class, true);

		criterion.like(ShopOrderBillExportDTO::getFlag, query.getFlag(), MatchMode.ANYWHERE)
				.like(ShopOrderBillExportDTO::getSn, query.getSn(), MatchMode.ANYWHERE)
				.eq(ShopOrderBillExportDTO::getStatus, query.getStatus());


		if (StringUtils.isNotBlank(query.getStartTime())) {
			criterion
					.ge(ShopOrderBillExportDTO::getCreateDate, DateUtil.beginOfDay(DateUtil.parse(query.getStartTime())))
					.le(ShopOrderBillExportDTO::getCreateDate, DateUtil.endOfDay(DateUtil.parse(query.getStartTime())));
		}
		criterion.addDescOrder(ShopOrderBillExportDTO::getCreateDate);
		return queryDTOByProperties(criterion);
	}

	@Override
	public int shopConfirm(Long id, Long shopId) {
		String sql = "update ls_shop_order_bill set status = ?, shop_confirm_date = ? where id = ? and shop_id = ?";
		return update(sql, ShopOrderBillStatusEnum.SHOP_CONFIRM.value(), new Date(), id, shopId);
	}

	@Override
	public int adminCheck(Long id) {
		String sql = "update ls_shop_order_bill set status = ?, platform_audit_date = ? where id = ?";
		return update(sql, ShopOrderBillStatusEnum.ADMIN_CONFIRM.value(), new Date(), id);
	}

	@Override
	public ShopOrderBillDTO getshopOrderBillCount(Long shopId) {

		return get(getSQL("ShopOrderBill.getshopOrderBillCount"), ShopOrderBillDTO.class, shopId);
	}

}
