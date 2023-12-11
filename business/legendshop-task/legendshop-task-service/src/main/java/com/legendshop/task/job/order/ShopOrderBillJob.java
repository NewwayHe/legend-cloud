/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.order.api.OrderApi;
import com.legendshop.order.api.ShopOrderBillApi;
import com.legendshop.order.enums.BillPeriodEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 账单结算定时器
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShopOrderBillJob {

	private final SysParamsApi sysParamsApi;
	private final OrderApi orderApi;
	private final ShopOrderBillApi shopOrderBillApi;

	/**
	 * 订单自动收货补偿定时任务
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("autoConfirmReceipt")
	public ReturnT<String> autoConfirmReceipt(String param) {
		log.info("============== 进入定时任务 autoConfirmReceipt 订单自动收货补偿 =====================");

		R<Void> result = orderApi.autoConfirmReceiptJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("订单自动收货补偿定时任务处理失败");
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}

	/**
	 * 账单结算定时器（结算上一周的）
	 * 每天凌晨1点进行结算
	 * 0 0 0 * * ?
	 *
	 * @param param todo 可以传入时间参数，进行指定日期的补偿 D：按天结算  W：按周结算  M：按月结算
	 * @return
	 * @throws Exception
	 */
	@XxlJob("calculateBill")
	public ReturnT<String> calculateBill(String param) {
		XxlJobHelper.log("开始账单结算");
		log.info("账单结算");
		//获取结算周期的时间
		Date endDate = getEndDate(param);
		if (ObjectUtil.isNull(endDate)) {
			return ReturnT.SUCCESS;
		}
		Date startDate = getStartDate(endDate, param);

		// TODO 处理方法
		R<Void> result = shopOrderBillApi.calculateBillJobHandle(startDate, endDate);
		if (!result.success()) {
			XxlJobHelper.log("账单结算定时任务处理失败--" + result.getMsg());
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}


	/**
	 * 获取结算的结束时间
	 *
	 * @return 上期结算的终止日期
	 */
	public Date getEndDate(String param) {
		Date endDate = null;
		//获取当前结算的周期类型
		BillPeriodEnum type = null;
		List<SysParamItemDTO> items = sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.BILL_PERIOD_TYPE.name()).getData();
		if (CollUtil.isNotEmpty(items)) {
			type = BillPeriodEnum.instance(items.get(0).getValue());
		}

		if (type == null) {
			type = BillPeriodEnum.instance("WEEK");
		}

		//用手动调用job输入类型覆盖掉原来的类型
		if (ObjectUtil.isNotEmpty(param)) {
			String[] split = param.split(",");
			if ("D".equals(split[0])) {
				type = BillPeriodEnum.DAY;
			} else if ("W".equals(split[0])) {
				type = BillPeriodEnum.WEEK;
			} else if ("M".equals(split[0])) {
				type = BillPeriodEnum.MONTH;
			}
			//获取传入的时间，没有则使用当前时间
			if (split.length > 1) {
				endDate = DateUtil.parse(split[1]);
			} else {
				endDate = DateUtil.date();
			}
		}

		//没传入参数则正常判断
		if (ObjectUtil.isNull(param)) {
			//判断结算周期类型
			if (BillPeriodEnum.MONTH.equals(type)) {
				int day = DateUtil.thisDayOfMonth();
				if (day > 1) {
					XxlJobHelper.log("账单结算周期为：每月1号结算，当前时间不是1号");
					log.info("账单结算周期为：每月1号结算，当前时间不是1号");
					return null;
				}
			} else if (BillPeriodEnum.WEEK.equals(type)) {
				int day = DateUtil.thisDayOfWeek();
				if (day != 2) {
					XxlJobHelper.log("账单结算周期为：每周一结算，当前时间不是周一");
					log.info("账单结算周期为：每周一结算，当前时间不是周一");
					return null;
				}
			}
		}

		//用于决定日结是每多少天结算一次, 默认是一天
		int settleInDays = 1;

		// 日结
		if (BillPeriodEnum.DAY.equals(type)) {
			Date myDate = new Date();
			Calendar cal = Calendar.getInstance();
			//判断是否有传入的时间，有则使用传入的时间
			if (ObjectUtil.isNull(endDate)) {
				cal.setTime(myDate);
			} else {
				cal.setTime(endDate);
			}
			cal.add(Calendar.DATE, -settleInDays);
			// 将时分秒,毫秒域清零
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			endDate = cal.getTime();

			// 周结
		} else if (BillPeriodEnum.WEEK.equals(type)) {
			// 设置时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//判断是否有传入的时间，有则使用传入的时间
			if (ObjectUtil.isNotNull(endDate)) {
				cal.setTime(endDate);
			}
			// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			// 获得当前日期是一个星期的第几天
			int day = cal.get(Calendar.DAY_OF_WEEK);
			int intevalDays = cal.getFirstDayOfWeek() - day;
			if (intevalDays > 0) {
				// 确保是上一周
				intevalDays = intevalDays - 7;
			}
			// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			cal.add(Calendar.DATE, intevalDays);
			try {
				endDate = sdf.parse(sdf.format(cal.getTime()));
			} catch (ParseException e) {
				throw new BusinessException("date format exception");
			}

			// 月结 找出结算月的第一天作为上期结算的最后一天，以自然月来计算
		} else {
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			try {
				//判断是否有传入的时间，有则使用传入的时间
				if (ObjectUtil.isNotNull(endDate)) {
					endDate = df.parse(df.format(endDate));
				} else {
					endDate = df.parse(df.format(new Date()));
				}
			} catch (ParseException e) {
				throw new BusinessException("date format exception");
			}
		}
		return endDate;
	}

	/**
	 * 获取结算的开始时间
	 */
	public Date getStartDate(Date endDate, String param) {

		Date startDate = null;

		//获取当前结算的周期类型
		BillPeriodEnum type = null;
		List<SysParamItemDTO> items = sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.BILL_PERIOD_TYPE.name()).getData();
		if (CollUtil.isNotEmpty(items)) {
			type = BillPeriodEnum.instance(items.get(0).getValue());
		}

		if (type == null) {
			type = BillPeriodEnum.instance("WEEK");
		}

		if (ObjectUtil.isNotEmpty(param)) {
			String[] split = param.split(",");
			if ("D".equals(split[0])) {
				type = BillPeriodEnum.DAY;
			} else if ("W".equals(split[0])) {
				type = BillPeriodEnum.WEEK;
			} else if ("M".equals(split[0])) {
				type = BillPeriodEnum.MONTH;

			}
		}
		//用于决定日结是每多少天结算一次, 默认是一天
		int settleInDays = 1;
		// 日结
		if (BillPeriodEnum.DAY.equals(type)) {
			// 往前推n天
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, -settleInDays);
			startDate = c.getTime();
		}
		// 周结
		else if (BillPeriodEnum.WEEK.equals(type)) {
			// 往前推一周
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.WEEK_OF_YEAR, -1);
			startDate = c.getTime();

		}
		// 月结
		else {
			// 往前推一个月
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.MONTH, -1);
			return c.getTime();
		}

		return startDate;
	}
}
