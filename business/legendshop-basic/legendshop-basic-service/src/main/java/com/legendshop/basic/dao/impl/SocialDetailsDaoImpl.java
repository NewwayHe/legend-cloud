/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;


import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.SocialDetailsDao;
import com.legendshop.basic.entity.SocialDetails;
import com.legendshop.basic.query.SocialDetailsQuery;
import org.springframework.stereotype.Repository;

/**
 * 社交登录的详情dao
 *
 * @author legendshop
 */
@Repository
public class SocialDetailsDaoImpl extends GenericDaoImpl<SocialDetails, Long> implements SocialDetailsDao {


	@Override
	public PageSupport<SocialDetails> page(SocialDetailsQuery socialDetailsQuery) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(SocialDetails.class, socialDetailsQuery.getPageSize(), socialDetailsQuery.getCurPage());
		criteriaQuery.eq("type", socialDetailsQuery.getType());
		return this.queryPage(criteriaQuery);
	}
}
