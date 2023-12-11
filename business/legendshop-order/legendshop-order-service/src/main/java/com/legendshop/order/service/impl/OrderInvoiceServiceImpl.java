/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dao.OrderInvoiceDao;
import com.legendshop.order.dto.OrderInvoiceDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.enums.OrderInvoiceFlagEnum;
import com.legendshop.order.excel.OrderInvoiceExportDTO;
import com.legendshop.order.query.OrderInvoiceQuery;
import com.legendshop.order.service.OrderInvoiceService;
import com.legendshop.order.service.convert.OrderInvoiceConverter;
import com.legendshop.user.enums.UserInvoiceTitleTypeEnum;
import com.legendshop.user.enums.UserInvoiceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单发票订单信息服务.
 *
 * @author legendshop
 */
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

	@Autowired
	private OrderInvoiceDao orderInvoiceDao;

	@Autowired
	private OrderInvoiceConverter orderInvoiceConverter;


	@Override
	public OrderInvoiceDTO getById(Long id) {
		return orderInvoiceConverter.to(orderInvoiceDao.getById(id));
	}

	@Override
	public Boolean deleteById(Long id) {
		return orderInvoiceDao.deleteById(id) > 0;
	}

	@Override
	public Long save(OrderInvoiceDTO orderInvoiceDTO) {
		return orderInvoiceDao.save(orderInvoiceConverter.from(orderInvoiceDTO));
	}

	@Override
	public Boolean update(OrderInvoiceDTO orderInvoiceDTO) {
		return orderInvoiceDao.update(orderInvoiceConverter.from(orderInvoiceDTO)) > 0;
	}

	@Override
	public PageSupport<OrderInvoiceDTO> queryPage(OrderInvoiceQuery orderInvoiceQuery) {

		PageSupport<OrderInvoiceDTO> ps = orderInvoiceDao.queryPage(orderInvoiceQuery);
		List<OrderInvoiceDTO> orderInvoiceDTOList = ps.getResultList();

		if (CollUtil.isEmpty(orderInvoiceDTOList)) {
			return ps;
		}
		Map<Long, List<OrderItemDTO>> orderItemList = orderInvoiceDao.queryUserInvoiceOrderPics().stream().filter(Objects::nonNull).collect(Collectors.groupingBy(OrderItemDTO::getOrderId));

		for (OrderInvoiceDTO orderInvoiceDTO : orderInvoiceDTOList) {

			orderInvoiceDTO.setOrderProductNum(orderInvoiceDTO.getProductQuantity());
			orderInvoiceDTO.setOrderPrice(orderInvoiceDTO.getTotalPrice());

			if (orderItemList.containsKey(orderInvoiceDTO.getId())) {
				List<OrderItemDTO> itemDTOS = orderItemList.get(orderInvoiceDTO.getId());
				List<String> priList = itemDTOS.stream().map(OrderItemDTO::getPic).collect(Collectors.toList());
				orderInvoiceDTO.setOrderProductPics(priList);
			}
		}
		return ps;
	}

	@Override
	public List<OrderInvoiceExportDTO> orderInvoiceExport(OrderInvoiceQuery query) {
		List<OrderInvoiceExportDTO> result = orderInvoiceDao.orderInvoiceExport(query);
		result.forEach(e -> {
			e.setType(UserInvoiceTypeEnum.getDes(e.getType()));
			e.setTitleType(UserInvoiceTitleTypeEnum.getDes(e.getTitleType()));
			e.setHasInvoiceFlag(OrderInvoiceFlagEnum.getDes(Integer.valueOf(e.getHasInvoiceFlag())));
		});
		return result;
	}
}
