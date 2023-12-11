/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service;

import com.legendshop.common.core.service.BaseService;
import com.legendshop.data.dto.CouponViewDTO;

/**
 * (CouponView)表服务接口
 *
 * @author legendshop
 * @since 2022-04-06 11:49:51
 */
public interface CouponViewService extends BaseService<CouponViewDTO> {

	void updateVisit(CouponViewDTO couponViewDTO);

}
