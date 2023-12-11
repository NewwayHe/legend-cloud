/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.activity;

import com.legendshop.activity.api.CouponApi;
import com.legendshop.common.core.constant.R;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 优惠券定时器
 *
 * @author legendshop
 */
@Component
@AllArgsConstructor
@Slf4j
public class CouponJob {

	final CouponApi couponApi;

	/**
	 * 优惠券到点上线
	 */
	@XxlJob("couponOnLine")
	public ReturnT<String> couponOnLine(String param) throws Exception {

		XxlJobHelper.log("couponOnLine-JOB, 定时执行:优惠券到点上线.");
		log.info("couponOnLine-JOB, 定时执行:优惠券到点上线");

		R<Void> result = couponApi.couponOnLineJobHandle();

		if (!result.success()) {
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 优惠券到点下线
	 */
	@XxlJob("couponOffLine")
	public ReturnT<String> couponOffLine(String param) throws Exception {
		XxlJobHelper.log("couponOffLine-JOB,定时执行:优惠券到点下线");
		log.info("couponOffLine-JOB,定时执行:优惠券到点下线");

		R<Void> result = couponApi.couponOffLineJobHandle();

		if (!result.success()) {
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 用户优惠券到点上线
	 */
	@XxlJob("userCouponValid")
	public ReturnT<String> userCouponValid(String param) throws Exception {
		XxlJobHelper.log("userCouponValid-JOB,定时执行:用户优惠券到点上线");
		log.info("userCouponValid-JOB,定时执行:用户优惠券到点上线");

		R<Void> result = couponApi.userCouponValidJobHandle();

		if (!result.success()) {
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 用户优惠券到点下线
	 */
	@XxlJob("userCouponInvalid")
	public ReturnT<String> userCouponInvalid(String param) throws Exception {
		XxlJobHelper.log("userCouponInvalid-JOB, 定时执行:用户优惠券到点下线");
		log.info("userCouponInvalid-JOB, 定时执行:用户优惠券到点下线");

		R<Void> result = couponApi.userCouponInvalidJobHandle();

		if (!result.success()) {
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}
}
