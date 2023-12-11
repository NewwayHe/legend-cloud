/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.shop.dao.IndustryDirectoryDao;
import com.legendshop.shop.entity.IndustryDirectory;
import com.legendshop.shop.query.IndustryDirectoryQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
@Repository
public class IndustryDirectoryDaoImpl extends GenericDaoImpl<IndustryDirectory, Long> implements IndustryDirectoryDao {

	/**
	 * 默认获取可用的行业目录
	 */
	@Override
	public List<IndustryDirectory> availableList(IndustryDirectoryQuery query) {
		EntityCriterion entityCriterion = new EntityCriterion();
		if (null == query.getState()) {
			entityCriterion.eq("state", Boolean.TRUE);
		} else {
			entityCriterion.eq("state", query.getState());
		}
		if (StringUtils.isNotBlank(query.getName())) {
			entityCriterion.like("name", query.getName(), MatchMode.ANYWHERE);
		}
		return super.queryByProperties(entityCriterion);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PageSupport<IndustryDirectory> pageList(IndustryDirectoryQuery query) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(IndustryDirectory.class, query.getPageSize(), query.getCurPage());
		if (null != query.getState()) {
			criteriaQuery.eq("state", query.getState());
		}
		if (StringUtils.isNotBlank(query.getName())) {
			criteriaQuery.like("name", query.getName(), MatchMode.ANYWHERE);
		}
		criteriaQuery.addAscOrder("seq");
		criteriaQuery.addDescOrder("createTime");
		return super.queryPage(criteriaQuery);
	}

	@Override
	public List<IndustryDirectory> queryById(List<Long> ids) {
		LambdaEntityCriterion<IndustryDirectory> criterion = new LambdaEntityCriterion<>(IndustryDirectory.class);
		criterion.in(IndustryDirectory::getId, ids);
		return queryByProperties(criterion);
	}
}
