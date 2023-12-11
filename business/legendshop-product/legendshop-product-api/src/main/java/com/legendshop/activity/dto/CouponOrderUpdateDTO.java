/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * (CouponOrder)DTO
 * 更新订单优惠券使用明细数据
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
@Data
public class CouponOrderUpdateDTO extends BaseDTO implements Serializable {
	/**
	 * 已选优惠券集合
	 */
	private List<CouponOrderDTO> couponOrderList;
	/**
	 * 优惠券使用明细
	 */
	private List<CouponItemDTO> selectCouponItem;
	/**
	 * 已选优惠券集合ID
	 */
	private List<Long> couponUserItemIds;

}
