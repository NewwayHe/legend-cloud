/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import java.util.Date;

/**
 * DomainDao接口.
 *
 * @author legendshop
 */
public interface DomainDao {

	/**
	 * 查询该二级域名是否已经绑定
	 *
	 * @param domainName
	 * @return true, if successful
	 */
	boolean queryDomainNameBinded(String domainName);

	/**
	 * 更新二级域名
	 *
	 * @param userId
	 * @param domainName
	 * @return
	 */
	int updateDomainName(Long userId, String domainName, Date registDate);

}
