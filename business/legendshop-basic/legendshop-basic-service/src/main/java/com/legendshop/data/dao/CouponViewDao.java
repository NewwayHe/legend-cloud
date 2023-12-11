/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.data.entity.CouponView;

import java.util.Date;

/**
 * (CouponView)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-06 11:49:51
 */
public interface CouponViewDao extends GenericDao<CouponView, Long> {


	CouponView queryVisit(Long couponId, String source, Date createTime);

}
