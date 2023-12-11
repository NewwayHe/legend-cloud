/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;


import com.legendshop.order.dto.OrderRefundReturnDetailDTO;

import java.util.List;

/**
 * 退款明细服务
 *
 * @author legendshop
 */
public interface OrderRefundReturnDetailService {

	/**
	 * 根据Id获取
	 */
	OrderRefundReturnDetailDTO getById(Long id);

	/**
	 * 删除
	 */
	void deleteById(Long id);

	/**
	 * 保存
	 */
	Long save(OrderRefundReturnDetailDTO subRefundReturnDetail);

	/**
	 * 更新
	 */
	void update(OrderRefundReturnDetailDTO subRefundReturnDetail);

	/**
	 * 根据refundId获取
	 */
	List<OrderRefundReturnDetailDTO> queryByRefundId(Long refundId);
}
