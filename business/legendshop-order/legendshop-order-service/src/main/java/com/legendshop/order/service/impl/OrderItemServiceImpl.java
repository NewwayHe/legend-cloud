/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.dao.OrderItemDao;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.QuotaOrderDTO;
import com.legendshop.order.entity.OrderItem;
import com.legendshop.order.query.OrderItemQuery;
import com.legendshop.order.service.OrderItemService;
import com.legendshop.order.service.convert.OrderItemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单项服务.
 *
 * @author legendshop
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemDao orderItemDao;

	@Autowired
	private OrderItemConverter orderItemConverter;

	@Override
	public OrderItemDTO getById(Long id) {
		return orderItemConverter.to(orderItemDao.getById(id));
	}

	@Override
	public List<OrderItemDTO> queryById(List<Long> ids) {
		return orderItemConverter.to(orderItemDao.queryAllByIds(ids));
	}

	@Override
	public Boolean update(OrderItemDTO orderItemDTO) {
		return orderItemDao.update(orderItemConverter.from(orderItemDTO)) > 0;
	}

	@Override
	public int update(List<OrderItemDTO> orderItemDTOS) {
		return orderItemDao.update(orderItemConverter.from(orderItemDTOS));
	}


	@Override
	public List<OrderItemDTO> getByOrderNumber(String orderNumber) {
		return orderItemConverter.to(orderItemDao.getByOrderNumber(orderNumber));
	}


	@Override
	public PageSupport<OrderItemDTO> queryOrderItems(OrderItemQuery orderItemQuery) {
		PageSupport<OrderItemDTO> page = orderItemConverter.page(orderItemDao.queryOrderItems(orderItemQuery));
		List<OrderItemDTO> resultList = page.getResultList();
		resultList.forEach(o -> {
			o.setProductPic(o.getPic());
		});
		return page;
	}


	@Override
	public PageSupport<OrderItemDTO> queryOrderItemsProd(OrderItemQuery orderItemQuery) {
		PageSupport<OrderItemDTO> page = orderItemConverter.page(orderItemDao.queryOrderItemsProd(orderItemQuery));
		List<OrderItemDTO> resultList = page.getResultList();
		resultList.forEach(o -> {
			o.setProductPic(o.getPic());
		});
		return page;
	}


	@Override
	public boolean updateOrderItemCommFlag(Integer status, Long orderItemId, Long userId) {
		return orderItemDao.updateOrderItemCommFlag(status, orderItemId, userId);
	}

	@Override
	public Boolean saveOrderItems(List<OrderItemDTO> orderItemDTOS) {

		List<OrderItem> orderItems = orderItemConverter.from(orderItemDTOS);
		List<Long> result = orderItemDao.save(orderItems);
		if (result.size() <= 0) {
			return false;
		}

		Map<String, Long> map = orderItems.stream().collect(Collectors.toMap(OrderItem::getOrderItemNumber, OrderItem::getId));
		for (OrderItemDTO orderItemDTO : orderItemDTOS) {
			if (map.containsKey(orderItemDTO.getOrderItemNumber())) {
				orderItemDTO.setId(map.get(orderItemDTO.getOrderItemNumber()));
			}
		}
		return true;
	}

	@Override
	public R<Void> updateProductSnapshot(String orderItemNumber, Long productSnapshotId) {

		OrderItem orderItem = orderItemDao.getOrderItemByOrderItemNumber(orderItemNumber);
		if (ObjectUtil.isNull(orderItem)) {
			return R.fail("订单项不存在");
		}
		orderItem.setSnapshotId(productSnapshotId);
		int update = orderItemDao.update(orderItem);
		if (update <= 0) {
			return R.fail("订单项更新失败");
		}
		return R.ok();
	}

	@Override
	public List<OrderItemDTO> queryByUserId(Long userId) {
		return orderItemDao.queryByUserId(userId);
	}

	@Override
	public List<OrderItemDTO> queryByOrderNumber(String orderNumber) {
		return orderItemDao.queryByOrderNumber(orderNumber);
	}

	@Override
	public int updateCommissionSettleStatus(List<String> distributionOrderItemNumbers, Integer value) {
		return orderItemDao.updateCommissionSettleStatus(distributionOrderItemNumbers, value);
	}

	@Override
	public List<OrderItemDTO> queryByOrderNumbers(List<String> orderNumbers) {
		return orderItemDao.getByOrderNumbers(orderNumbers);
	}

	@Override
	public List<OrderItemDTO> getExpiredDivideOrder() {
		return orderItemDao.getExpiredDivideOrder();
	}

	@Override
	public R<List<OrderItemDTO>> queryByNumberList(List<String> numberList) {
		if (CollectionUtils.isEmpty(numberList)) {
			return R.ok(new ArrayList<>());
		}
		List<OrderItem> itemList = this.orderItemDao.getByOrderNumberList(numberList);
		return R.ok(this.orderItemConverter.to(itemList));
	}


	@Override
	public Integer getQuotaorderSUM(QuotaOrderDTO quotaOrderDTO) {
		Date beginTime = null;
		Date endTime = null;
		if (ObjectUtil.isEmpty(quotaOrderDTO.getQuotaType())) {
			return null;
		}
		switch (quotaOrderDTO.getQuotaType()) {
			case "D":
				beginTime = DateUtil.beginOfDay(new Date());
				endTime = DateUtil.endOfDay(new Date());
				break;
			case "W":
				beginTime = DateUtil.beginOfWeek(new Date());
				endTime = DateUtil.endOfWeek(new Date());
				break;
			case "M":
				beginTime = DateUtil.beginOfMonth(new Date());
				endTime = DateUtil.endOfMonth(new Date());
				break;
			case "Y":
				beginTime = DateUtil.beginOfYear(new Date());
				endTime = DateUtil.endOfYear(new Date());
				break;
			case "A":
				break;
			default:
				return quotaOrderDTO.getQuotaCount();
		}
		return orderItemDao.getPayedQuptaOrderStatisticsByUserId(quotaOrderDTO, beginTime, endTime);
	}

	@Override
	public List<OrderItemDTO> queryReturnDeadlineIsNull() {
		return orderItemDao.queryReturnDeadlineIsNull();
	}
}
