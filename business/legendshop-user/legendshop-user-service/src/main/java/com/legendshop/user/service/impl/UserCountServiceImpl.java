/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.data.dto.DataUserAmountDTO;
import com.legendshop.user.bo.RegisterCountBO;
import com.legendshop.user.dao.ShopUserDao;
import com.legendshop.user.dao.UserCountDao;
import com.legendshop.user.dto.*;
import com.legendshop.user.query.*;
import com.legendshop.user.service.UserCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 用户统计服务
 *
 * @author legendshop
 */
@Slf4j
@Service
public class UserCountServiceImpl implements UserCountService {

	@Autowired
	private UserCountDao userCountDao;

	@Autowired
	private ShopUserDao shopCountDao;

	@Override
	public UserDataNewDTO getNewUserData(String source) {

		UserDataNewDTO dto = new UserDataNewDTO();
		//新增用户数量
		Date[] date = getDate(1);
		Date s = date[0];
		Date e = date[1];
		Integer total = userCountDao.getNewUserData(source, null, e);
		dto.setNewUser(total);

		//日增量
		Integer newUserData = userCountDao.getNewUserData(source, s, e);
		Date s1 = DateUtil.offset(s, DateField.DAY_OF_YEAR, -1);
		Date e1 = DateUtil.offset(e, DateField.DAY_OF_YEAR, -1);
		Integer num = userCountDao.getNewUserData(source, s1, e1);
		int dayNum = newUserData - num;
		dto.setNewUserByDay(dayNum);

		//周增量
		Date[] date1 = getDate(7);
		Date s2 = date1[0];
		Date e2 = date1[1];
		//本周增量
		Integer dataWeek = userCountDao.getNewUserData(source, s2, e2);
		//上周增量
		Date s7 = DateUtil.offset(s2, DateField.DAY_OF_YEAR, -7);
		Date e7 = DateUtil.offset(e2, DateField.DAY_OF_YEAR, -7);
		Integer dataWeek1 = userCountDao.getNewUserData(source, s7, e7);
		int weekNum = dataWeek - dataWeek1;
		dto.setNewUserByWeek(weekNum);

		//月增量
		Date[] date2 = getDate(30);
		Date s3 = date2[0];
		Date e3 = date2[1];
		//本月增量
		Integer dataMonth = userCountDao.getNewUserData(source, s3, e3);
		//上月增量
		DateTime s30 = DateUtil.offset(s3, DateField.DAY_OF_YEAR, -30);
		DateTime e30 = DateUtil.offset(e3, DateField.DAY_OF_YEAR, -30);
		Integer dataMonth1 = userCountDao.getNewUserData(source, s30, e30);
		int monthNum = dataMonth - dataMonth1;
		dto.setNewUserByMonth(monthNum);

		return dto;
	}

	@Override
	public UserDataAllDTO getTotalUserData(String source) {


		UserDataAllDTO dto = new UserDataAllDTO();
		//用户总数量
		Date[] date = getDate(1);
		Date s = date[0];
		Date e = date[1];
		Integer allUserData = userCountDao.getTotalUserData(source, null, e);
		dto.setAllUserNum(allUserData);


		Integer newUserData = userCountDao.getTotalUserData(source, s, e);
		Date s1 = DateUtil.offset(s, DateField.DAY_OF_YEAR, -1);
		Date e1 = DateUtil.offset(e, DateField.DAY_OF_YEAR, -1);
		Integer num = userCountDao.getTotalUserData(source, s1, e1);
		int dayNum = newUserData - num;
		dto.setAllUserNumByDay(dayNum);


		Date[] date1 = getDate(7);
		Date s2 = date1[0];
		Date e2 = date1[1];
		//本周增量
		Integer dataWeek = userCountDao.getTotalUserData(source, s2, e2);
		//上周增量
		Date s7 = DateUtil.offset(s2, DateField.DAY_OF_YEAR, -7);
		Date e7 = DateUtil.offset(e2, DateField.DAY_OF_YEAR, -7);
		Integer dataWeek1 = userCountDao.getTotalUserData(source, s7, e7);
		int weekNum = dataWeek - dataWeek1;
		dto.setAllUserNumByWeek(weekNum);


		Date[] date2 = getDate(30);
		Date s3 = date2[0];
		Date e3 = date2[1];
		//本月增量
		Integer dataMonth = userCountDao.getTotalUserData(source, s3, e3);
		//上月增量
		DateTime s30 = DateUtil.offset(s3, DateField.DAY_OF_YEAR, -30);
		DateTime e30 = DateUtil.offset(e3, DateField.DAY_OF_YEAR, -30);
		Integer dataMonth1 = userCountDao.getTotalUserData(source, s30, e30);
		int monthNum = dataMonth - dataMonth1;
		dto.setAllUserNumByMonth(monthNum);

		return dto;
	}

	@Override
	public UserDataConsumerDTO getConsumerData(String source) {

		UserDataConsumerDTO dto = new UserDataConsumerDTO();
		//消费用户数量
		Date[] date = getDate(1);
		Date s = date[0];
		Date e = date[1];
		Integer consumerUser = userCountDao.getConsumerData(source, null, e);
		dto.setConsumerUser(consumerUser);

		//日增量
		Integer newUserData = userCountDao.getConsumerData(source, s, e);
		Date s1 = DateUtil.offset(s, DateField.DAY_OF_YEAR, -1);
		Date e1 = DateUtil.offset(e, DateField.DAY_OF_YEAR, -1);
		Integer num = userCountDao.getConsumerData(source, s1, e1);
		int dayNum = newUserData - num;
		dto.setConsumerUserByDay(dayNum);

		//周增量
		Date[] date1 = getDate(7);
		Date s2 = date1[0];
		Date e2 = date1[1];
		//本周增量
		Integer dataWeek = userCountDao.getConsumerData(source, s2, e2);
		//上周增量
		Date s7 = DateUtil.offset(s2, DateField.DAY_OF_YEAR, -7);
		Date e7 = DateUtil.offset(e2, DateField.DAY_OF_YEAR, -7);
		Integer dataWeek1 = userCountDao.getConsumerData(source, s7, e7);
		int weekNum = dataWeek - dataWeek1;
		dto.setConsumerUserByWeek(weekNum);

		//月增量
		Date[] date2 = getDate(30);
		Date s3 = date2[0];
		Date e3 = date2[1];
		//本月增量
		Integer dataMonth = userCountDao.getConsumerData(source, s3, e3);
		//上月增量
		DateTime s30 = DateUtil.offset(s3, DateField.DAY_OF_YEAR, -30);
		DateTime e30 = DateUtil.offset(e3, DateField.DAY_OF_YEAR, -30);
		Integer dataMonth1 = userCountDao.getConsumerData(source, s30, e30);
		int monthNum = dataMonth - dataMonth1;
		dto.setConsumerUserByMonth(monthNum);

		return dto;
	}

	@Override
	public List<UserDataAreaDTO> getDistributedArea() {
		Date date = getDate(1)[1];
		return userCountDao.queryDistributedArea(date);
	}


	@Override
	public PageSupport<UserDataAreaDTO> getDistributedAreaPage(UserDataAreaQuery userDataAreaQuery) {
		if (ObjectUtil.isNotEmpty(userDataAreaQuery.getStartDate()) && ObjectUtil.isNotEmpty(userDataAreaQuery.getEndDate())) {
			userDataAreaQuery.setStartDate(DateUtil.beginOfDay(userDataAreaQuery.getStartDate()));
			userDataAreaQuery.setEndDate(DateUtil.endOfDay(userDataAreaQuery.getEndDate()));
		} else {
			Date date = getDate(1)[1];
			userDataAreaQuery.setEndDate(date);
			userDataAreaQuery.setStartDate(null);
		}
		return userCountDao.queryDistributedAreaPage(userDataAreaQuery);
	}

	@Override
	public List<UserDataAreaDTO> getDistributedAreaExcel(UserDataAreaQuery userDataAreaQuery) {
		if (ObjectUtil.isNotEmpty(userDataAreaQuery.getStartDate()) && ObjectUtil.isNotEmpty(userDataAreaQuery.getEndDate())) {
			userDataAreaQuery.setStartDate(DateUtil.beginOfDay(userDataAreaQuery.getStartDate()));
			userDataAreaQuery.setEndDate(DateUtil.endOfDay(userDataAreaQuery.getEndDate()));
		} else {
			Date date = getDate(1)[1];
			userDataAreaQuery.setEndDate(date);
			userDataAreaQuery.setStartDate(null);
		}
		return userCountDao.getDistributedAreaExcel(userDataAreaQuery);
	}

	@Override
	public List<UserDataGradeDTO> getDistributedGrade() {

		Date date = getDate(1)[1];
		Integer count = userCountDao.queryUserCount(date);
		List<UserDataGradeDTO> dto = userCountDao.queryDistributedGrade(date);
		dto.forEach(a -> {
			if (a.getGrade() == null) {
				a.setGrade(0);
			}
			double per = (double) a.getUserNum() / count;
			a.setPercentage(per);
		});

		return dto;
	}

	@Override
	public List<UserDataPurchasingPowerDTO> getPurchasingPower() {
		Date date = getDate(1)[1];
		List<UserDataPurchasingPowerDTO> list = userCountDao.queryPurchasingPower(date);
		list.forEach(a -> {
			a.setMobile(getDesensitization(a.getMobile()));
		});
		return list;
	}

	@Override
	public List<UserDataShopSaleDTO> getShopSaleData() {
		Date date = getDate(1)[1];
		return userCountDao.queryShopSaleData(date);
	}

	@Override
	public List<DataUserAmountDTO> getUserAmountLine(UserCountAmountQuery amountQuery) {
		List<Date> dateAvg;
		if ((amountQuery.getEndDate() == null && amountQuery.getStartDate() == null) || (amountQuery.getEndDate().isEmpty() && amountQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			dateAvg = getDateAvg(s, e);
		} else {
			//设置了日期范围
			Date s = DateUtil.parseDateTime(amountQuery.getStartDate());
			Date e = DateUtil.parseDateTime(amountQuery.getEndDate());
			dateAvg = getDateAvg(s, e);
		}
		List<DataUserAmountDTO> list = new ArrayList<>();
		for (int x = 0; x < dateAvg.size() - 1; x++) {
			amountQuery.setStartDate(DateUtil.formatDateTime(dateAvg.get(x)));
			amountQuery.setEndDate(DateUtil.formatDateTime(DateUtil.endOfDay(dateAvg.get(x))));
			DataUserAmountDTO dto = userCountDao.getUserAmountLine(amountQuery);
			if (dto.getPeopleAmount() == null) {
				dto = new DataUserAmountDTO(0);
			}
			dto.setPeopleNew(dto.getH5New() + dto.getAppNew() + dto.getMiniNew());
			dto.setCreateTime(dateAvg.get(x));
			list.add(dto);
		}
		return list;
	}

	@Override
	public PageSupport<DataUserAmountDTO> getUserAmountPage(UserCountAmountQuery amountQuery) {
		amountQuery.setProp(checkString(amountQuery.getProp()));
		if ((amountQuery.getEndDate() == null && amountQuery.getStartDate() == null) || (amountQuery.getEndDate().isEmpty() && amountQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			amountQuery.setStartDate(DateUtil.formatDateTime(s));
			amountQuery.setEndDate(DateUtil.formatDateTime(e));
		}

		PageSupport<DataUserAmountDTO> page = userCountDao.queryUserAmountPage(amountQuery);
		if (CollUtil.isNotEmpty(page.getResultList())) {
			for (DataUserAmountDTO dto : page.getResultList()) {
				dto.setPeopleNew(dto.getH5New() + dto.getAppNew() + dto.getMiniNew());
			}
		}
		return page;
	}

	@Override
	public List<DataUserAmountDTO> getUserAmountPageExcel(UserCountAmountQuery amountQuery) {
		amountQuery.setProp(checkString(amountQuery.getProp()));
		if ((amountQuery.getEndDate() == null && amountQuery.getStartDate() == null) || (amountQuery.getEndDate().isEmpty() && amountQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			amountQuery.setStartDate(DateUtil.formatDateTime(s));
			amountQuery.setEndDate(DateUtil.formatDateTime(e));
		}

		List<DataUserAmountDTO> dataUserAmountDTOS = userCountDao.queryUserAmountPageExcel(amountQuery);
		if (CollUtil.isNotEmpty(dataUserAmountDTOS)) {
			for (DataUserAmountDTO dto : dataUserAmountDTOS) {
				dto.setPeopleNew(dto.getH5New() + dto.getAppNew() + dto.getMiniNew());
			}
		}
		return dataUserAmountDTOS;
	}

	@Override
	public List<UserDataPurchasingPageDTO> getPurchasingPic(UserPurchasingQuery purchasingQuery) {
		purchasingQuery.setProp(checkString(purchasingQuery.getProp()));
		if ((purchasingQuery.getEndDate() == null && purchasingQuery.getStartDate() == null) || (purchasingQuery.getEndDate().isEmpty() && purchasingQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			purchasingQuery.setStartDate(DateUtil.formatDateTime(s));
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(purchasingQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		List<UserDataPurchasingPageDTO> list = userCountDao.queryPurchasingPic(purchasingQuery);
		list.forEach(dto -> {
			//手机号码脱敏
			String desensitization = getDesensitization(dto.getMobile());
			dto.setMobile(desensitization);
		});
		return list;
	}

	@Override
	public PageSupport<UserDataPurchasingPageDTO> getPurchasingPage(UserPurchasingQuery purchasingQuery) {
		purchasingQuery.setProp(checkString(purchasingQuery.getProp()));
		if ((purchasingQuery.getEndDate() == null && purchasingQuery.getStartDate() == null) || (purchasingQuery.getEndDate().isEmpty() && purchasingQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			purchasingQuery.setStartDate(DateUtil.formatDateTime(s));
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(purchasingQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		PageSupport<UserDataPurchasingPageDTO> list = userCountDao.queryPurchasingPage(purchasingQuery);
		list.getResultList().forEach(dto -> {
			//手机号码脱敏
			String desensitization = getDesensitization(dto.getMobile());
			dto.setMobile(desensitization);
		});
		return list;
	}

	@Override
	public List<UserDataPurchasingPageDTO> getPurchasingPageExcel(UserPurchasingQuery purchasingQuery) {
		purchasingQuery.setProp(checkString(purchasingQuery.getProp()));
		if ((purchasingQuery.getEndDate() == null && purchasingQuery.getStartDate() == null) || (purchasingQuery.getEndDate().isEmpty() && purchasingQuery.getStartDate().isEmpty())) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			purchasingQuery.setStartDate(DateUtil.formatDateTime(s));
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		} else {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(purchasingQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			purchasingQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		List<UserDataPurchasingPageDTO> list = userCountDao.queryPurchasingPageExcel(purchasingQuery);
		list.forEach(dto -> {
			//设置客单价
			if (!(new BigDecimal(0).equals(dto.getDealAmount())) && dto.getDealQuantity() != 0) {
				dto.setCustomerPrice(dto.getDealAmount().divide(BigDecimal.valueOf(dto.getDealQuantity()), 2, RoundingMode.HALF_DOWN));
			} else {
				dto.setCustomerPrice(new BigDecimal(0));
			}
			//手机号码脱敏
			String desensitization = getDesensitization(dto.getMobile());
			dto.setMobile(desensitization);
		});
		return list;
	}

	@Override
	public PageSupport<LoginHistoryDTO> getLoginHistoryPage(UserCountLoginQuery loginQuery) {
		if (StrUtil.isNotBlank(loginQuery.getStartDate()) && StrUtil.isNotBlank(loginQuery.getEndDate())) {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(loginQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			loginQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		return userCountDao.queryLoginHistoryPage(loginQuery);
	}

	@Override
	public PageSupport<LoginHistoryCountDTO> getLoginHistoryCountPage(UserCountLoginQuery loginQuery) {
		if (ObjectUtil.isNotEmpty(loginQuery.getStartDate()) && ObjectUtil.isNotEmpty(loginQuery.getEndDate())) {
			//设置了日期范围
			Date e = DateUtil.parseDateTime(loginQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			loginQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		return userCountDao.queryLoginHistoryCountPage(loginQuery);
	}

	@Override
	public PageSupport<UserDataSmsDTO> getSmsHistory(UserCountSmsQuery smsQuery) {

		if (!((smsQuery.getEndDate() == null && smsQuery.getStartDate() == null) || (smsQuery.getEndDate().isEmpty() && smsQuery.getStartDate().isEmpty()))) {

			//设置了日期范围
			Date e = DateUtil.parseDateTime(smsQuery.getEndDate());
			e = DateUtil.endOfDay(e);
			smsQuery.setEndDate(DateUtil.formatDateTime(e));
		}
		Integer status = smsQuery.getStatus();
		PageSupport<UserDataSmsDTO> pageSupport = null;
		if (ObjectUtil.isNotEmpty(status)) {
			if (status == -1) {
				smsQuery.setFail("OK");
			} else if (status == 1) {
				smsQuery.setSuccess("OK");
			}
		}
		pageSupport = userCountDao.querySmsHistory(smsQuery);
		if (pageSupport != null) {
			pageSupport.getResultList().forEach(a -> {
				if ("OK".equals(a.getResponse())) {
					a.setStatus("成功");
				} else {
					a.setStatus("失败");
				}
			});
		}
		return pageSupport;
	}

	@Override
	public PageSupport<UserDataShopSalePageDTO> getShopSalePage(UserCountShopSaleQuery saleQuery) {
		if ((saleQuery.getEndDate() == null && saleQuery.getStartDate() == null)) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			saleQuery.setStartDate(s);
			saleQuery.setEndDate(e);
		} else {
			//设置了日期范围
			saleQuery.setEndDate(DateUtil.endOfDay(saleQuery.getEndDate()));
		}
		saleQuery.setProp(checkString(saleQuery.getProp()));
		PageSupport<UserDataShopSalePageDTO> shopSalePage = userCountDao.getShopSalePage(saleQuery);
		BigDecimal hundred = new BigDecimal(100);
		shopSalePage.getResultList().forEach(a -> {
			BigDecimal orderTime = Optional.ofNullable(a.getOrderResponseTime()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN);
			a.setOrderResponseTime(orderTime);
			a.setUndercarriageRate(Optional.ofNullable(a.getUndercarriageRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			a.setOutStockRate(Optional.ofNullable(a.getOutStockRate()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN));
		});
		return shopSalePage;
	}

	@Override
	public List<UserDataShopSalePageDTO> getShopSalePic(UserCountShopSaleQuery saleQuery) {
		if ((saleQuery.getEndDate() == null && saleQuery.getStartDate() == null)) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			saleQuery.setStartDate(s);
			saleQuery.setEndDate(e);
		} else {
			//设置了日期范围
			saleQuery.setEndDate(DateUtil.endOfDay(saleQuery.getEndDate()));
		}
		saleQuery.setProp(checkString(saleQuery.getProp()));
		List<UserDataShopSalePageDTO> shopSalePic = userCountDao.getShopSalePic(saleQuery);
		BigDecimal hundred = new BigDecimal(100);
		shopSalePic.forEach(a -> {
			BigDecimal orderTime = Optional.ofNullable(a.getOrderResponseTime()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN);
			a.setOrderResponseTime(orderTime);
			a.setUndercarriageRate(Optional.ofNullable(a.getUndercarriageRate()).orElse(BigDecimal.ZERO).multiply(hundred).setScale(2, RoundingMode.HALF_DOWN));
			a.setOutStockRate(Optional.ofNullable(a.getOutStockRate()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN));
		});
		return shopSalePic;
	}

	@Override
	public List<UserDataShopSalePageDTO> getShopSalePageExcel(UserCountShopSaleQuery saleQuery) {
		if ((saleQuery.getEndDate() == null && saleQuery.getStartDate() == null)) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			saleQuery.setStartDate(s);
			saleQuery.setEndDate(e);
		} else {
			//设置了日期范围
			saleQuery.setEndDate(DateUtil.endOfDay(saleQuery.getEndDate()));
		}
		saleQuery.setProp(checkString(saleQuery.getProp()));
		List<UserDataShopSalePageDTO> shopSalePageExcel = userCountDao.getShopSalePageExcel(saleQuery);
		shopSalePageExcel.forEach(a -> {
			BigDecimal orderTime = Optional.ofNullable(a.getOrderResponseTime()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN);
			a.setOrderResponseTime(orderTime);
			BigDecimal stockRate = Optional.ofNullable(a.getOutStockRate()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN);
			a.setOutStockRate(stockRate);
			BigDecimal UnderRate = Optional.ofNullable(a.getUndercarriageRate()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_DOWN);
			a.setUndercarriageRate(UnderRate);
		});
		return shopSalePageExcel;
	}

	/**
	 * 手机号码脱敏
	 *
	 * @param mobile
	 * @return
	 */
	private String getDesensitization(String mobile) {
		if (StrUtil.isBlank(mobile) || mobile.length() < 11) {
			return "";
		}
		return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
	}


	/**
	 * 根据天数获取--前num天至昨天23点59分59秒的date数组。
	 *
	 * @param num 天数
	 * @return date[0]-开始时间  date[1]-结束时间
	 */
	private Date[] getDate(Integer num) {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);

		Date beginOfDay = DateUtil.beginOfDay(date);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] array = new Date[2];
		array[0] = startDate;
		array[1] = endDate;

		return array;
	}

	/**
	 * 将时间范围分成十份，不能均分时添加随机元素，不足十天时根据一天分割。
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateAvg(Date startDate, Date endDate) {
		int between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> list = new ArrayList<>();
		int avg;
		if (between < 10) {
			for (int x = 1; x <= between; x++) {
				list.add(1);
			}
			avg = 1;
		} else {
			double v = between / 9.0;
			int i = between % 9;
			Integer floor = (int) Math.floor(v);
			for (int x = 1; x < 10; x++) {
				list.add(floor);
			}
			avg = floor;
			while (i > 0) {
				int random = (int) Math.floor(Math.random() * 9);
				Integer now = list.get(random);
				if (now > floor) {
					continue;
				}
				list.set(random, now + 1);
				i--;
			}
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		list.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		dateList.add(DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, avg));
		return dateList;
	}

	/**
	 * prop转成数据库的字段
	 *
	 * @param str
	 * @return
	 */
	private String checkString(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		if (str.length() <= 1) {
			return str.toLowerCase(Locale.ROOT);
		}
		char ch;
		int num = 0;
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				result.append(str.substring(num, i)).append("_").append((char) (ch + 32));
				num = i + 1;
			}
		}
		if (num < str.length()) {
			result.append(str.substring(num));
		}
		return result.toString();
	}

	@Override
	public RegisterCountBO getRegisterCount() {

		//新增用户数量
		Date[] date = getDate(1);
		Date now = DateUtil.parse(DateUtil.now());
		DateTime beginOfDay = DateUtil.beginOfDay(now);

		Integer userRegisterToday = userCountDao.getNewUserData(null, beginOfDay, now);

		Integer userRegisterTotal = userCountDao.getNewUserData(null, null, now);

		Integer shopRegisterToday = shopCountDao.getNewShopData(beginOfDay, now);

		Integer shopRegisterTotal = shopCountDao.getNewShopData(null, now);

		return RegisterCountBO
				.builder()
				.userRegisterToday(userRegisterToday)
				.userRegisterTotal(userRegisterTotal)
				.shopRegisterToday(shopRegisterToday)
				.shopRegisterTotal(shopRegisterTotal)
				.build();
	}
}
