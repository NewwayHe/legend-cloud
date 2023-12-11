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
import com.legendshop.activity.dao.ActivityCountDao;
import com.legendshop.activity.dto.ActivityAnalysisDTO;
import com.legendshop.activity.dto.ActivityUsageCountDTO;
import com.legendshop.activity.enums.CouponProviderEnum;
import com.legendshop.activity.enums.MarketingTypeEnum;
import com.legendshop.activity.query.MarketingDataViewQuery;
import com.legendshop.common.datasource.NonTable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ActivityCountDaoImpl extends GenericDaoImpl<NonTable, Long> implements ActivityCountDao {

	/*------------------------------------------------新增使用次数-----------------------------------------------------------------------------------*/

	/**
	 * type为null走折线
	 */
	@Override
	public List<ActivityUsageCountDTO> marketingRewardUsageCount(MarketingDataViewQuery viewQuery, Integer type) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT COUNT(*) AS `usage` ");
		if (type == null) {
			stringBuffer.append(", DATE(create_time) AS date ");
		}
		stringBuffer.append("FROM ls_marketing_reward lmr WHERE create_time >= ? AND create_time <= ? ");
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND shop_id = ? ");
			args.add(viewQuery.getShopId());
		}
		if (type == null) {
			stringBuffer.append("GROUP BY DATE(create_time)");
		} else {
			stringBuffer.append("AND lmr.`type`=? ");
			args.add(type);
		}
		return super.query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());
	}

	/**
	 * boot为true走表格
	 */
	@Override
	public List<ActivityUsageCountDTO> marketingLimitUsageCount(MarketingDataViewQuery viewQuery, Boolean boot) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT COUNT(*) AS `usage`");
		if (!boot) {
			stringBuffer.append(", DATE(create_time) AS date ");
		}
		stringBuffer.append("FROM ls_marketing_limit_discounts lmd WHERE create_time >=? AND create_time <=? ");
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND shop_id = ?");
			args.add(viewQuery.getShopId());
		}
		if (!boot) {
			stringBuffer.append("GROUP BY DATE(create_time)");
		}
		return super.query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());
	}

	@Override
	public List<ActivityUsageCountDTO> marketingCouponCount(MarketingDataViewQuery viewQuery, CouponProviderEnum coupon, Boolean boot) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT COUNT(*) AS `usage`, DATE(create_time)AS DATE FROM ls_coupon lc WHERE lc.`shop_provider_flag`=? AND create_time >=? AND create_time <=? ");
		args.add(coupon.getValues());
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND shop_id = ?");
			args.add(viewQuery.getShopId());
		}
		//折线
		if (!boot) {
			stringBuffer.append("GROUP BY DATE(create_time)");
		}
		return super.query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());
	}

	/*-------------------------------------------------新增成交金额,成交数量,成交用户数,新成交用户数,旧成交用户数,累计访问次数，累计访问用户数，转化率----------------------------------------------------------------------------------*/

	@Override
	public List<ActivityUsageCountDTO> marketingRewardDealCount(MarketingDataViewQuery viewQuery, Integer type) {

		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT ");
		//折线： 查满减满折每天金额
		if (type == null) {
			stringBuffer.append("SUM(actual_amount) AS amount, DATE(lo.create_time)AS DATE FROM ls_order lo LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` ");
			//表格： 分别查满减、满折时间段的总金额、成交数量
		} else {
			stringBuffer.append("t.`type` AS TYPE,SUM(amount) AS amount, SUM(`count`) AS `count`,COUNT(`dealUser`) AS dealUserNum,COUNT(DISTINCT `dealUser`) AS newUserNum, SUM(CASE WHEN t.`count`>1 THEN 1 ELSE 0 END )AS oldUserNum, 1 AS totalView, 10 AS totalViewPeople, IFNULL(COUNT(`dealUser`)/10,0) AS inversionRate  FROM " +
					"(SELECT lmr.`type` AS TYPE,SUM(actual_amount) AS amount, COUNT(DISTINCT lo.id) AS `count`, lo.user_id AS dealUser FROM ls_order lo " +
					"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
					"LEFT JOIN ls_marketing_reward lmr ON loi.`reward_marketing_id`=lmr.`id` ");
		}
		stringBuffer.append("WHERE lo.`status`=20 " +
				"AND lo.`refund_status`!= 1 " +
				"AND  loi.`reward_marketing_id` IS NOT NULL " +
				"AND  lo.create_time >=? " +
				"AND lo.create_time <=? ");
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND lo.shop_id = ? ");
			args.add(viewQuery.getShopId());
		}
		if (type == null) {
			stringBuffer.append("GROUP BY DATE(lo.create_time) ");
		} else {
			stringBuffer.append("AND  lmr.`type`=? GROUP BY lo.user_id) t");
			args.add(type);
		}
		return query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());


	}

	/**
	 * boot为false则走折线图
	 */
	@Override
	public List<ActivityUsageCountDTO> marketingLimitDealCount(MarketingDataViewQuery viewQuery, Boolean boot) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT ");
		//表格： 查限时折扣时间段总金额
		if (boot) {
			stringBuffer.append("SUM(amount) AS amount, SUM(`count`) AS `count`,COUNT(`dealUser`) AS dealUserNum,COUNT(DISTINCT `dealUser`) AS newUserNum, SUM(CASE WHEN t.`count`>1 THEN 1 ELSE 0 END )AS oldUserNum, 1 AS totalView, 10 AS totalViewPeople, IFNULL(COUNT(`dealUser`)/10,0) AS inversionRate  FROM " +
					"(SELECT SUM(actual_amount)AS amount , COUNT(DISTINCT lo.id) AS `count`,lo.user_id AS dealUser " +
					" FROM ls_order lo ");
			//折线： 分别查限时折扣每天金额
		} else {
			stringBuffer.append("SUM(actual_amount) AS amount, DATE(lo.create_time)AS DATE FROM ls_order lo ");
		}
		stringBuffer.append(
				"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
						"WHERE lo.`status`=20 " +
						"AND lo.`refund_status`!= 1 " +
						"AND  loi.`limit_discounts_marketing_id` IS NOT NULL " +
						"AND  lo.create_time >=? " +
						"AND lo.create_time <=? ");
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND lo.shop_id = ? ");
			args.add(viewQuery.getShopId());
		}
		if (boot) {
			stringBuffer.append("GROUP BY lo.user_id ) t");
		} else {
			stringBuffer.append("GROUP BY DATE(lo.create_time)");
		}
		return query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());
	}

	@Override
	public List<ActivityUsageCountDTO> marketingCouponDealCount(MarketingDataViewQuery viewQuery, CouponProviderEnum coupon, Boolean boot) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT ");
		//表格
		if (boot) {
			stringBuffer.append("SUM(amount) AS amount, SUM(`count`) AS `count`,COUNT(`dealUser`) AS dealUserNum,COUNT(DISTINCT `dealUser`) AS newUserNum, SUM(CASE WHEN t.`count`>1 THEN 1 ELSE 0 END )AS oldUserNum, 1 AS totalView, 10 AS totalViewPeople, IFNULL(COUNT(`dealUser`)/10,0) AS inversionRate   FROM " +
					"(SELECT SUM(actual_amount) AS amount, COUNT(DISTINCT lo.id) AS `count`,lo.user_id AS dealUser " +
					"FROM ls_order lo " +
					"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
					"WHERE lo.`status`=20 " +
					"AND lo.`refund_status`!= 1 ");
			//折线
		} else {
			stringBuffer.append("SUM(actual_amount) AS amount, DATE(lo.create_time)AS DATE FROM ls_order lo " +
					"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
					"WHERE lo.`status`=20 " +
					"AND lo.`refund_status`!= 1 ");
		}
		if (coupon.getValues().equals(0)) {
			stringBuffer.append("AND loi.`platform_coupon_off_price` IS NOT NULL AND loi.`platform_coupon_off_price` !=0 ");
		}
		if (coupon.getValues().equals(1)) {
			stringBuffer.append("AND loi.`coupon_off_price` IS NOT NULL AND loi.`coupon_off_price` !=0 ");
		}
		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND lo.shop_id = ? ");
			args.add(viewQuery.getShopId());
		}
		if (boot) {
			stringBuffer.append("AND  lo.create_time >=? AND lo.create_time <=? GROUP BY lo.user_id) t");
		} else {
			stringBuffer.append("AND  lo.create_time >=? AND lo.create_time <=? GROUP BY DATE(lo.create_time)");
		}
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		return query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());

	}

	@Override
	public List<ActivityUsageCountDTO> activityDealCount(MarketingDataViewQuery viewQuery, MarketingTypeEnum activity, Boolean boot) {
		List<Object> args = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer("SELECT ");
		//表格
		if (boot) {
			stringBuffer.append(
					"SUM(amount) AS amount, SUM(`count`) AS `count`,COUNT(`dealUser`) AS dealUserNum,COUNT(DISTINCT `dealUser`) AS newUserNum, SUM(CASE WHEN t.`count`>1 THEN 1 ELSE 0 END )AS oldUserNum, 1 AS totalView, 10 AS totalViewPeople, IFNULL(COUNT(`dealUser`)/10,0) AS inversionRate  FROM " +
							"		(SELECT SUM(actual_amount) AS amount,COUNT(DISTINCT lo.id) AS `count`,lo.user_id  AS dealUser FROM ls_order lo " +
							"				LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` WHERE lo.`status`=20 AND lo.`refund_status`!= 1 ");
		}
		//折线
		else {
			stringBuffer.append(
					"SUM(actual_amount) AS amount, DATE(lo.create_time)AS DATE FROM ls_order lo LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` WHERE lo.`status`=20 AND lo.`refund_status`!= 1 ");
		}

		if (viewQuery.getShopId() != null) {
			stringBuffer.append("AND lo.shop_id = ? ");
			args.add(viewQuery.getShopId());
		}
		if (boot) {
			stringBuffer.append("AND  lo.create_time >=? AND lo.create_time <=? GROUP BY lo.user_id) t");
		} else {
			stringBuffer.append("AND  lo.create_time >=? AND lo.create_time <=? GROUP BY DATE(lo.create_time)");
		}
		args.add(viewQuery.getStartDate());
		args.add(viewQuery.getEndDate());
		return query(stringBuffer.toString(), ActivityUsageCountDTO.class, args.toArray());

	}


	@Override
	public List<ActivityAnalysisDTO> activitypayCount(MarketingDataViewQuery viewQuery) {
		return super.query("SELECT IFNULL(loi.product_total_amount,0) AS productTotalAmount, IFNULL(loi.actual_amount,0) AS actualAmount FROM ls_order lo " +
				"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
				"WHERE  (order_type='S' OR order_type='MG' OR order_type='G' OR loi.reward_marketing_id IS NOT NULL OR limit_discounts_marketing_id IS NOT NULL OR (coupon_off_price IS NOT NULL AND loi.`coupon_off_price` !=0)) " +
				"AND  lo.create_time >=? " +
				"AND lo.create_time <=? " +
				"AND lo.`shop_id`=? ", ActivityAnalysisDTO.class, viewQuery.getStartDate(), viewQuery.getEndDate(), viewQuery.getShopId());
	}

	@Override
	public ActivityAnalysisDTO activityDealRate(MarketingDataViewQuery viewQuery) {
		return super.get("SELECT   COUNT(DISTINCT lo.id) AS `count`, SUM(basket_count) AS totalCount FROM ls_order lo " +
				"LEFT JOIN ls_order_item loi ON lo.id=loi.`order_id` " +
				"WHERE (order_type='S' OR order_type='MG' OR order_type='G' OR loi.reward_marketing_id IS NOT NULL OR limit_discounts_marketing_id IS NOT NULL OR (coupon_off_price IS NOT NULL AND loi.`coupon_off_price` !=0)) " +
				"AND lo.`status`=20 " +
				"AND lo.`refund_status`!= 1 " +
				"AND  lo.create_time >=? " +
				"AND lo.create_time <=? " +
				"AND lo.`shop_id`=? ", ActivityAnalysisDTO.class, viewQuery.getStartDate(), viewQuery.getEndDate(), viewQuery.getShopId());
	}


}
