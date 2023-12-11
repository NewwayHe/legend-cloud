/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.shop.entity.AdvertiseCount;
import com.legendshop.shop.query.AdvertiseQuery;

import java.util.Date;
import java.util.List;

/**
 * (AdvertiseCount)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-27 17:21:40
 */
public interface AdvertiseCountDao extends GenericDao<AdvertiseCount, Long> {


	AdvertiseCount queryFrequency(Date createTime, Long advertiseId, String source);


	List<AdvertiseCount> queryAdvertiseCount(AdvertiseQuery query);

	List<AdvertiseCount> queryAdvertiseCountList(AdvertiseQuery query);

}
