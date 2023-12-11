/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.entity.SocialDetails;
import com.legendshop.basic.query.SocialDetailsQuery;

/**
 * 社交登录dao
 *
 * @author legendshop
 */
public interface SocialDetailsDao extends GenericDao<SocialDetails, Long> {

	/**
	 * 分页查询
	 *
	 * @param socialDetailsQuery
	 * @return
	 */
	PageSupport<SocialDetails> page(SocialDetailsQuery socialDetailsQuery);

}
