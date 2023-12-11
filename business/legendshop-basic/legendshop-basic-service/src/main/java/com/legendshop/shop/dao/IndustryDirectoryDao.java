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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.entity.IndustryDirectory;
import com.legendshop.shop.query.IndustryDirectoryQuery;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
public interface IndustryDirectoryDao extends GenericDao<IndustryDirectory, Long> {


	List<IndustryDirectory> availableList(IndustryDirectoryQuery query);

	PageSupport<IndustryDirectory> pageList(IndustryDirectoryQuery query);

	List<IndustryDirectory> queryById(List<Long> ids);
}
