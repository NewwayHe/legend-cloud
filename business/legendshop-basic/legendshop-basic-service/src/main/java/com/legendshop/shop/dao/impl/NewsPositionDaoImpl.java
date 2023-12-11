/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.shop.dao.NewsPositionDao;
import com.legendshop.shop.entity.NewsPosition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 新闻位置.
 *
 * @author legendshop
 */
@Repository
public class NewsPositionDaoImpl extends GenericDaoImpl<NewsPosition, Long> implements NewsPositionDao {

	@Override
	public List<NewsPosition> getNewsPositionByNewsId(Long newsId) {
		return queryByProperties(new EntityCriterion().eq("newsid", newsId));
	}

}
