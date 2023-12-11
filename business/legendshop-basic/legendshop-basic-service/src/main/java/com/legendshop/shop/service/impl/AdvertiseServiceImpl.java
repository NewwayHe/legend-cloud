/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.enums.AdvertiseStatusEnum;
import com.legendshop.basic.api.AmqpTaskApi;
import com.legendshop.basic.dto.AmqpTaskDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.rabbitmq.util.AmqpSendMsgUtil;
import com.legendshop.shop.constants.AmqpConst;
import com.legendshop.shop.dao.AdvertiseDao;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.dto.AdvertiseStausCountDTO;
import com.legendshop.shop.entity.Advertise;
import com.legendshop.shop.query.AdvertiseQuery;
import com.legendshop.shop.service.AdvertiseService;
import com.legendshop.shop.service.convert.AdvertiseConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (Advertise)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-27 15:23:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertiseServiceImpl implements AdvertiseService {

	@Autowired
	private AdvertiseDao advertiseDao;
	@Autowired
	private AmqpSendMsgUtil amqpSendMsgUtil;
	@Autowired
	private AdvertiseConverter converter;
	@Autowired
	final AmqpTaskApi amqpTaskApi;


	@Override
	public R saveAdvertise(AdvertiseDTO advertiseDTO) {
		if (!advertiseDTO.getStartTime().before(advertiseDTO.getEndTime())) {
			return R.fail("结束时间需要大于开始时间");
		}
		Advertise advertise = converter.from(advertiseDTO);
		if (CollUtil.isNotEmpty(advertiseDTO.getSourceList())) {
			StringBuffer stringBuffer = new StringBuffer();
			List<String> list = advertiseDTO.getSourceList();
			for (String s : list) {
				stringBuffer.append(s);
				stringBuffer.append(",");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			advertise.setSource(stringBuffer.toString());
		}
		//获取时间差
		long between = DateUtil.between(DateUtil.date(), advertiseDTO.getStartTime(), DateUnit.SECOND, false);
		//设置活动状态
		advertise.setStatus(1);

		if (between > 0) {
			advertise.setStatus(0);
		}
		Long save = advertiseDao.save(advertise);

		AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
		if (between > 0) {
			amqpTaskDTO.setExchange(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_START_ROUTING_KEY);
			amqpTaskDTO.setMessage(String.valueOf(save));
			amqpTaskDTO.setDelayTime(advertiseDTO.getStartTime());
			amqpTaskApi.convertAndSend(amqpTaskDTO);
		}
		amqpTaskDTO.setExchange(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE);
		amqpTaskDTO.setRoutingKey(com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_END_ROUTING_KEY);
		amqpTaskDTO.setMessage(String.valueOf(save));
		amqpTaskDTO.setDelayTime(advertiseDTO.getEndTime());
		amqpTaskApi.convertAndSend(amqpTaskDTO);
		return R.ok();
	}


	@Override
	public R update(AdvertiseDTO advertiseDTO) {


		//前端点击编辑按钮
		if (ObjectUtil.isNotEmpty(advertiseDTO.getEndTime())) {
			Advertise from = converter.from(advertiseDTO);
			if (CollUtil.isNotEmpty(advertiseDTO.getSourceList())) {
				StringBuffer stringBuffer = new StringBuffer();
				List<String> list = advertiseDTO.getSourceList();
				for (String s : list) {
					stringBuffer.append(s);
					stringBuffer.append(",");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1);
				from.setSource(stringBuffer.toString());
			}


			AmqpTaskDTO amqpTaskDTO = new AmqpTaskDTO();
			amqpTaskDTO.setExchange(com.legendshop.common.rabbitmq.constants.AmqpConst.DELAY_EXCHANGE);
			amqpTaskDTO.setRoutingKey(com.legendshop.common.rabbitmq.constants.AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_END_ROUTING_KEY);
			amqpTaskDTO.setMessage(String.valueOf(advertiseDTO.getId()));
			amqpTaskDTO.setDelayTime(advertiseDTO.getEndTime());
			amqpTaskApi.convertAndSend(amqpTaskDTO);
			return R.ok(advertiseDao.update(from));

		}
		//前端点击修改状态
		Advertise advertise = advertiseDao.getById(advertiseDTO.getId());
		if (CollUtil.isNotEmpty(advertiseDTO.getSourceList())) {
			StringBuffer stringBuffer = new StringBuffer();
			List<String> list = advertiseDTO.getSourceList();
			for (String s : list) {
				stringBuffer.append(s);
				stringBuffer.append(",");
			}
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
			advertise.setSource(stringBuffer.toString());
		}
		if (ObjectUtil.isNotEmpty(advertiseDTO.getStatus())) {
			advertise.setStatus(advertiseDTO.getStatus());
			return R.ok(advertiseDao.update(advertise));
		}
		return R.ok();
	}

	@Override
	public List<AdvertiseDTO> queryAdvertise(AdvertiseQuery query) {
		List<Advertise> advertises = advertiseDao.queryAdvertise(query);
		List<AdvertiseDTO> to = converter.to(advertises);
		List<AdvertiseDTO> list = new ArrayList<>();
		//把String字段转化为list返回前端
		for (AdvertiseDTO advertiseDTO : to) {
			if (StrUtil.isNotBlank(advertiseDTO.getSource())) {
				String source = advertiseDTO.getSource();
				List<String> sourceList = Arrays.asList(source.split(","));
				advertiseDTO.setSourceList(sourceList);
				list.add(advertiseDTO);
			}
		}
		return list;
	}

	@Override
	public PageSupport<AdvertiseDTO> queryAdvertisePage(AdvertiseQuery query) {


		PageSupport<AdvertiseDTO> advertisePageSupport = advertiseDao.queryAdvertisePage(query);
		List<AdvertiseDTO> resultList = advertisePageSupport.getResultList();
		List<AdvertiseDTO> list = new ArrayList<>();


		//把String字段转化为list返回前端
		for (AdvertiseDTO advertiseDTO : resultList) {

			String source = advertiseDTO.getSource();
			List<String> sourceList = Arrays.asList(source.split(","));
			advertiseDTO.setSourceList(sourceList);
			list.add(advertiseDTO);

		}

		advertisePageSupport.setResultList(list);
		return advertisePageSupport;
	}

	@Override
	public PageSupport<AdvertiseCountDTO> queryAdvertiseDataReport(AdvertiseQuery query) {
		PageSupport<AdvertiseCountDTO> advertiseCountPageSupport = advertiseDao.queryAdvertiseDataReport(query);
		List<AdvertiseCountDTO> resultList = advertiseCountPageSupport.getResultList();
		ArrayList<AdvertiseCountDTO> advertiseCount = new ArrayList<>();
		for (AdvertiseCountDTO advertiseCountDTO : resultList) {
			//转化率
			advertiseCountDTO.setInversionRate(BigDecimal.ZERO);
			List<AdvertiseCountDTO> list = new ArrayList<>();

			if (ObjectUtil.isNotEmpty(advertiseCountDTO.getClickCount())
					&& ObjectUtil.isNotEmpty(advertiseCountDTO.getPutCount())
					&& BigDecimal.ZERO.compareTo(advertiseCountDTO.getPutCount()) != 0) {
				BigDecimal inversionRate = NumberUtil.div(advertiseCountDTO.getClickCount(), advertiseCountDTO.getPutCount())
						.multiply(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
				advertiseCountDTO.setInversionRate(inversionRate);

			}
			if (ObjectUtil.isNotEmpty(query.getSource())) {
				advertiseCountDTO.setSource(query.getSource());
			}
			advertiseCountPageSupport.setResultList(list);
			advertiseCount.add(advertiseCountDTO);
		}
		advertiseCountPageSupport.setResultList(advertiseCount);

		return advertiseCountPageSupport;
	}

	@Override
	public void updatePut(AdvertiseCountDTO advertiseDTO) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_SHOP_PUT_EXCHANGE, AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_PUT_LOG_ROUTING_KEY, advertiseDTO);
	}

	@Override
	public AdvertiseDTO getById(Long id) {
		Advertise byId = advertiseDao.getById(id);
		AdvertiseDTO advertiseDTO = converter.to(byId);
		//把String字段转化为list返回前端
		if (StrUtil.isNotBlank(byId.getSource())) {
			String source = byId.getSource();
			List<String> sourceList = Arrays.asList(source.split(","));
			advertiseDTO.setSourceList(sourceList);
		}


		return advertiseDTO;
	}

	@Override
	public Integer deleteById(Long id) {

		return advertiseDao.deleteById(id);
	}

	@Override
	public List<AdvertiseStausCountDTO> queryadvertiseCount(AdvertiseQuery query) {
		//获取所有状态的广告数量
		List<AdvertiseStausCountDTO> advertises = advertiseDao.queryAdvertiseAll();

		//遍历枚举防止有状态没有数据
		out:
		for (AdvertiseStatusEnum statusEnum : AdvertiseStatusEnum.values()) {
			for (AdvertiseStausCountDTO advertise : advertises) {
				if (advertise.getStatus().equals(statusEnum.value())) {
					continue out;
				}
			}
			advertises.add(new AdvertiseStausCountDTO(statusEnum.value(), 0));
		}
		return advertises;
	}

	@Override
	public void updateClock(AdvertiseCountDTO advertiseDTO) {
		amqpSendMsgUtil.convertAndSend(AmqpConst.LEGENDSHOP_SHOP_CLICK_EXCHANGE, AmqpConst.LEGENDSHOP_SHOP_ADVERTISE_LOG_CLICK_ROUTING_KEY, advertiseDTO);
	}


}
