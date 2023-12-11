/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.legendshop.jpaplus.GenericJdbcDao;
import com.legendshop.basic.dao.DomainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * DomainDao实现类
 *
 * @author legendshop
 */
@Repository
public class DomainDaoImpl implements DomainDao {

	@Autowired
	private GenericJdbcDao genericJdbcDao;

	/**
	 * 查询该二级域名是否已经绑定
	 */
	@Override
	public boolean queryDomainNameBinded(String domainName) {
		return genericJdbcDao.get(" select count(*) from ls_shop_detail where sec_domain_name = ?", Long.class, domainName) > 0;
	}

	/**
	 * 更新二级域名
	 */
	@Override
	public int updateDomainName(Long userId, String domainName, Date registDate) {
		return genericJdbcDao.update("update ls_shop_detail set sec_domain_name = ?,sec_domain_reg_date=? where user_id = ?", domainName, registDate, userId);
	}


}
