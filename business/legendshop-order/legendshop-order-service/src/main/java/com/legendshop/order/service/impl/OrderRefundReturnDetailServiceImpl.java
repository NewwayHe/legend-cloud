/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import com.legendshop.order.dao.OrderRefundReturnDetailDao;
import com.legendshop.order.dto.OrderRefundReturnDetailDTO;
import com.legendshop.order.service.OrderRefundReturnDetailService;
import com.legendshop.order.service.convert.OrderRefundReturnDetailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 退款明细服务
 *
 * @author legendshop
 */
@Service
public class OrderRefundReturnDetailServiceImpl implements OrderRefundReturnDetailService {

	/**
	 * 引用的Dao接口
	 */
	@Autowired
	private OrderRefundReturnDetailDao orderRefundReturnDetailDao;

	@Autowired
	private OrderRefundReturnDetailConverter orderRefundReturnDetailConverter;

	/**
	 * 根据Id获取
	 */
	@Override
	public OrderRefundReturnDetailDTO getById(Long id) {
		return orderRefundReturnDetailConverter.to(orderRefundReturnDetailDao.getById(id));
	}

	/**
	 * 删除
	 */
	@Override
	public void deleteById(Long id) {
		orderRefundReturnDetailDao.deleteById(id);
	}

	/**
	 * 保存
	 */
	@Override
	public Long save(OrderRefundReturnDetailDTO orderRefundReturnDetailDTO) {
		return orderRefundReturnDetailDao.save(orderRefundReturnDetailConverter.from(orderRefundReturnDetailDTO));
	}

	/**
	 * 更新
	 */
	@Override
	public void update(OrderRefundReturnDetailDTO orderRefundReturnDetailDTO) {
		orderRefundReturnDetailDao.update(orderRefundReturnDetailConverter.from(orderRefundReturnDetailDTO));
	}

	@Override
	public List<OrderRefundReturnDetailDTO> queryByRefundId(Long refundId) {
		return orderRefundReturnDetailConverter.to(orderRefundReturnDetailDao.queryByRefundId(refundId));
	}
}
