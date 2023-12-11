/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.dao.AdvertiseCountDao;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.entity.AdvertiseCount;
import com.legendshop.shop.excel.AdvertiseCountExportDTO;
import com.legendshop.shop.query.AdvertiseQuery;
import com.legendshop.shop.service.AdvertiseCountService;
import com.legendshop.shop.service.convert.AdvertiseCountConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * (AdvertiseCount)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-27 17:21:40
 */
@Slf4j
@Service
public class AdvertiseCountServiceImpl extends BaseServiceImpl<AdvertiseCountDTO, AdvertiseCountDao, AdvertiseCountConverter> implements AdvertiseCountService {
	@Resource
	private AdvertiseCountDao advertiseCountDao;
	@Autowired
	private AdvertiseCountConverter advertiseCountConverter;
	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void updateClick(AdvertiseCountDTO dto) {
		Date createTime = DateUtil.beginOfDay(dto.getCreateTime());
		Long advertiseId = dto.getAdvertiseId();
		String source = dto.getSource();
		Long userId = dto.getUserId();
		String userKey = dto.getUserKey();
		//查询广告数据
		AdvertiseCount advertiseCount = advertiseCountDao.queryFrequency(createTime, advertiseId, source);
		if (advertiseCount == null) {
			advertiseCount = new AdvertiseCount();
			dto.setPutPeople(BigDecimal.ZERO);
			dto.setPutCount(BigDecimal.ZERO);
			dto.setClickCount(BigDecimal.ZERO);
			dto.setClickPeople(BigDecimal.ZERO);
			dto.setCreateTime(createTime);
			Long id = advertiseCountDao.save(advertiseCountConverter.from(dto));
			dto.setId(id);
			BeanUtil.copyProperties(dto, advertiseCount);
		}
		long between = DateUtil.between(DateUtil.date(), DateUtil.beginOfDay(DateUtil.offsetDay(createTime, 1)), DateUnit.SECOND, true);
		log.info("离当天结束还有 {} 秒", between);
		if (userId == null) {
			switch (source) {
				case "PC":
					if (!redisTemplate.hasKey("PC" + userKey + dto.getAdvertiseId())) {
						advertiseCount.setClickPeople(advertiseCount.getClickPeople().add(BigDecimal.valueOf(1)));
						//保存记录到redis
						redisTemplate.opsForValue().set("PC" + userKey + advertiseId, 1, between, TimeUnit.SECONDS);

					}
					break;


				case "APP":
					if (!redisTemplate.hasKey("APP" + userKey + dto.getAdvertiseId())) {
						advertiseCount.setClickPeople(advertiseCount.getClickPeople().add(BigDecimal.valueOf(1)));
						//保存记录到redis
						redisTemplate.opsForValue().set("APP" + userKey + advertiseId, 1, between, TimeUnit.SECONDS);
					}
					break;

				default:
			}
		} else
		//数据库有广告记录，根据来源更新浏览信息
		{
			switch (source) {
				case "PC":
					if (!redisTemplate.hasKey("PC" + dto.getUserId() + dto.getAdvertiseId())) {
						advertiseCount.setClickPeople(advertiseCount.getClickPeople().add(BigDecimal.valueOf(1)));
						//保存记录到redis
						redisTemplate.opsForValue().set("PC" + userId + advertiseId, 1, between, TimeUnit.SECONDS);

					}
					break;


				case "APP":
					if (!redisTemplate.hasKey("APP" + dto.getUserId() + dto.getAdvertiseId())) {
						advertiseCount.setClickPeople(advertiseCount.getClickPeople().add(BigDecimal.valueOf(1)));
						//保存记录到redis
						redisTemplate.opsForValue().set("APP" + userId + advertiseId, 1, between, TimeUnit.SECONDS);
					}
					break;
				default:
			}
		}
		advertiseCount.setClickCount(advertiseCount.getClickCount().add(BigDecimal.valueOf(1)));
		advertiseCountDao.updateProperties(advertiseCount);
	}

	@Override
	public void updatePut(AdvertiseCountDTO dto) {
		Date createTime = DateUtil.beginOfDay(dto.getCreateTime());
		Long advertiseId = dto.getAdvertiseId();
		String source = dto.getSource();
		Long userId = dto.getUserId();

		AdvertiseCount advertiseCount = advertiseCountDao.queryFrequency(createTime, advertiseId, source);
		if (advertiseCount == null) {
			advertiseCount = new AdvertiseCount();
			dto.setPutPeople(BigDecimal.ZERO);
			dto.setPutCount(BigDecimal.ZERO);
			dto.setClickCount(BigDecimal.ZERO);
			dto.setClickPeople(BigDecimal.ZERO);
			dto.setCreateTime(createTime);
			Long id = advertiseCountDao.save(advertiseCountConverter.from(dto));
			dto.setId(id);
			BeanUtil.copyProperties(dto, advertiseCount);
		}
		long between = DateUtil.between(DateUtil.date(), DateUtil.beginOfDay(DateUtil.offsetDay(createTime, 1)), DateUnit.SECOND, true);
		log.info("离当天结束还有 {} 秒", between);
		if (userId == null) {
			//如果userid为null说明用户未登录
			String userKey = dto.getUserKey();
			//数据库有广告记录，根据来源更新浏览信息
			switch (source) {
				case "PC":
					if (!redisTemplate.hasKey(userKey + "PC" + dto.getAdvertiseId())) {
						advertiseCount.setPutPeople(advertiseCount.getPutPeople().add(BigDecimal.valueOf(1)));
						redisTemplate.opsForValue().set(userKey + "PC" + advertiseId, 1, between, TimeUnit.SECONDS);

					}
					break;


				case "APP":
					if (!redisTemplate.hasKey(userKey + "APP" + dto.getAdvertiseId())) {
						advertiseCount.setPutPeople(advertiseCount.getPutPeople().add(BigDecimal.valueOf(1)));
						redisTemplate.opsForValue().set(userKey + "APP" + advertiseId, 1, between, TimeUnit.SECONDS);
					}
					break;
				default:

			}
		} else {
			//数据库有广告记录，根据来源更新浏览信息
			switch (source) {
				case "PC":
					if (!redisTemplate.hasKey(userId + "PC" + dto.getAdvertiseId())) {
						advertiseCount.setPutPeople(advertiseCount.getPutPeople().add(BigDecimal.valueOf(1)));
						redisTemplate.opsForValue().set(userId + "PC" + advertiseId, 1, between, TimeUnit.SECONDS);

					}
					break;


				case "APP":
					if (!redisTemplate.hasKey(userId + "APP" + dto.getAdvertiseId())) {
						advertiseCount.setPutPeople(advertiseCount.getPutPeople().add(BigDecimal.valueOf(1)));
						redisTemplate.opsForValue().set(userId + "APP" + advertiseId, 1, between, TimeUnit.SECONDS);
					}
					break;
				default:

			}
		}

		advertiseCount.setPutCount(advertiseCount.getPutCount().add(BigDecimal.valueOf(1)));
		advertiseCountDao.updateProperties(advertiseCount);
	}

	@Override
	public List<AdvertiseCountDTO> queryAdvertiseCountReport(AdvertiseQuery query) {
		if (ObjectUtil.isEmpty(query.getId())) {
			return Collections.emptyList();
		}
		//默认显示七天数据
		List<Date> dateAvg;
		if (query.getEndTime() == null && query.getStartTime() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			query.setStartTime(s);
			query.setEndTime(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(query.getStartTime(), query.getEndTime());
		}
		List<AdvertiseCountDTO> advertiseCount = new ArrayList<>();
		for (Date currentDate : dateAvg) {
			query.setStartTime(DateUtil.beginOfDay(currentDate));
			query.setEndTime(DateUtil.endOfDay(currentDate));
			AdvertiseCountDTO advertiseCountDTO = advertiseCountDataMap(query);
			//添加数据
			advertiseCount.add(advertiseCountDTO);

		}
		return advertiseCount;

	}


	@Override
	public List<AdvertiseCountExportDTO> queryAdvertiseCountReportExport(AdvertiseQuery query) {
		if (ObjectUtil.isEmpty(query.getId())) {
			return Collections.emptyList();
		}
		//默认显示七天数据
		List<Date> dateAvg;
		if (query.getEndTime() == null && query.getStartTime() == null) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			query.setStartTime(s);
			query.setEndTime(DateUtil.endOfDay(e));
			dateAvg = getDateSplit(s, e);
		} else {
			dateAvg = getDateSplit(query.getStartTime(), query.getEndTime());
		}
		List<AdvertiseCountExportDTO> advertiseCount = new ArrayList<>();
		for (Date currentDate : dateAvg) {
			query.setStartTime(DateUtil.beginOfDay(currentDate));
			query.setEndTime(DateUtil.endOfDay(currentDate));
			AdvertiseCountExportDTO advertiseCountDTO = advertiseCountDataMapExport(query);
			//添加数据
			advertiseCount.add(advertiseCountDTO);
		}

		return advertiseCount;
	}


	private AdvertiseCountExportDTO advertiseCountDataMapExport(AdvertiseQuery query) {
		//组装数据
		AdvertiseCountExportDTO advertiseCountDTO = new AdvertiseCountExportDTO();
		advertiseCountDTO.setTime(DateUtil.formatDate(query.getStartTime()));
		List<AdvertiseCount> advertiseCount = advertiseCountDao.queryAdvertiseCount(query);
		List<AdvertiseCount> advertiseCountList = advertiseCountDao.queryAdvertiseCountList(query);
		if (advertiseCount == null) {
			advertiseCount = new ArrayList<>();
		}
		if (advertiseCountList == null) {
			advertiseCountList = new ArrayList<>();
		}
		AdvertiseCount advertiseList = new AdvertiseCount();
		if (CollUtil.isNotEmpty(advertiseCountList)) {
			advertiseList = advertiseCountList.get(0);
		}
		AdvertiseCount newAdvertise = new AdvertiseCount();

		if (CollUtil.isNotEmpty(advertiseCount)) {
			newAdvertise = advertiseCount.get(0);
		}
		//获取累计数据
		advertiseCountDTO.setClickCount(Optional.ofNullable(advertiseList).map(AdvertiseCount::getClickCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setClickPeople(Optional.ofNullable(advertiseList).map(AdvertiseCount::getClickPeople).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setPutCount(Optional.ofNullable(advertiseList).map(AdvertiseCount::getPutCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setPutPeople(Optional.ofNullable(advertiseList).map(AdvertiseCount::getPutPeople).orElse(BigDecimal.ZERO));
		//获取新增数据
		advertiseCountDTO.setNewPutCount(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getPutCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewClickCount(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getClickCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewClickPeople(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getClickPeople).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewPutPeople(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getPutPeople).orElse(BigDecimal.ZERO));

		//转化率
		advertiseCountDTO.setInversionRate(BigDecimal.ZERO);
		if (ObjectUtil.isNotEmpty(advertiseCountDTO.getClickCount())
				&& ObjectUtil.isNotEmpty(advertiseCountDTO.getPutCount())
				&& BigDecimal.ZERO.compareTo(advertiseCountDTO.getPutCount()) != 0) {
			BigDecimal inversionRate = NumberUtil.div(advertiseCountDTO.getClickCount(), advertiseCountDTO.getPutCount())
					.multiply(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
			advertiseCountDTO.setInversionRate(inversionRate);
		}
		return advertiseCountDTO;
	}


	private AdvertiseCountDTO advertiseCountDataMap(AdvertiseQuery query) {
		//组装数据
		AdvertiseCountDTO advertiseCountDTO = new AdvertiseCountDTO();
		advertiseCountDTO.setTime(DateUtil.formatDate(query.getStartTime()));
		List<AdvertiseCount> advertiseCount = advertiseCountDao.queryAdvertiseCount(query);
		List<AdvertiseCount> advertiseCountList = advertiseCountDao.queryAdvertiseCountList(query);
		if (advertiseCount == null) {
			advertiseCount = new ArrayList<>();
		}
		if (advertiseCountList == null) {
			advertiseCountList = new ArrayList<>();
		}
		AdvertiseCount advertiseList = new AdvertiseCount();
		if (CollUtil.isNotEmpty(advertiseCountList)) {
			advertiseList = advertiseCountList.get(0);
		}
		AdvertiseCount newAdvertise = new AdvertiseCount();
		if (CollUtil.isNotEmpty(advertiseCount)) {
			newAdvertise = advertiseCount.get(0);
		}
		//获取累计数据
		advertiseCountDTO.setClickCount(Optional.ofNullable(advertiseList).map(AdvertiseCount::getClickCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setClickPeople(Optional.ofNullable(advertiseList).map(AdvertiseCount::getClickPeople).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setPutCount(Optional.ofNullable(advertiseList).map(AdvertiseCount::getPutCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setPutPeople(Optional.ofNullable(advertiseList).map(AdvertiseCount::getPutPeople).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setId(advertiseList.getId());
		advertiseCountDTO.setCreateTime(advertiseList.getCreateTime());

		//获取新增数据
		advertiseCountDTO.setNewPutCount(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getPutCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewClickCount(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getClickCount).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewClickPeople(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getClickPeople).orElse(BigDecimal.ZERO));
		advertiseCountDTO.setNewPutPeople(Optional.ofNullable(newAdvertise).map(AdvertiseCount::getPutPeople).orElse(BigDecimal.ZERO));


		advertiseCountDTO.setAdvertiseId(query.getId());
		//转化率
		advertiseCountDTO.setInversionRate(BigDecimal.ZERO);
		if (ObjectUtil.isNotEmpty(advertiseCountDTO.getClickCount())
				&& ObjectUtil.isNotEmpty(advertiseCountDTO.getPutCount())
				&& BigDecimal.ZERO.compareTo(advertiseCountDTO.getPutCount()) != 0) {
			BigDecimal inversionRate = NumberUtil.div(advertiseCountDTO.getClickCount(), advertiseCountDTO.getPutCount())
					.multiply(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
			advertiseCountDTO.setInversionRate(inversionRate);
		}
		return advertiseCountDTO;

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
	 * 将日期分割成以天为单位的list
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<Date> getDateSplit(Date startDate, Date endDate) {
		Integer between = Math.toIntExact(DateUtil.between(startDate, endDate, DateUnit.DAY));
		List<Integer> integerList = new ArrayList<>();
		for (int x = 1; x <= between; x++) {
			integerList.add(1);
		}
		List<Date> dateList = new ArrayList<>();
		dateList.add(startDate);
		AtomicReference<Date> temp = new AtomicReference<>(startDate);
		integerList.forEach(a -> {
			Date offset = DateUtil.offset(temp.get(), DateField.DAY_OF_YEAR, a);
			temp.set(offset);
			dateList.add(offset);
		});
		return dateList;
	}
}
