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
import com.legendshop.shop.entity.NewsPosition;

import java.util.List;

/**
 * 新闻位置
 *
 * @author legendshop
 */
public interface NewsPositionDao extends GenericDao<NewsPosition, Long> {

	List<NewsPosition> getNewsPositionByNewsId(Long newsId);

}
