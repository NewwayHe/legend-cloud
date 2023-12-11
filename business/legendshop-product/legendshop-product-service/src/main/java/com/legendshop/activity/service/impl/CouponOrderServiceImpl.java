/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.activity.dao.CouponOrderDao;
import com.legendshop.activity.dto.CouponOrderDTO;
import com.legendshop.activity.entity.CouponOrder;
import com.legendshop.activity.service.CouponOrderService;
import com.legendshop.activity.service.convert.CouponOrderConverter;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (CouponOrder)表服务实现类
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
@AllArgsConstructor
@Slf4j
@Service
public class CouponOrderServiceImpl extends GenericDaoImpl<CouponOrder, Long> implements CouponOrderService {

	@Autowired
	private CouponOrderDao couponOrderDao;

	@Autowired
	private CouponOrderConverter couponOrderConverter;

	@Override
	public R<Void> saveCoupon(List<CouponOrderDTO> couponOrderList) {
		List<Long> ids = couponOrderDao.save(couponOrderConverter.from(couponOrderList));
		return R.process(CollUtil.isNotEmpty(ids), "");
	}


}
