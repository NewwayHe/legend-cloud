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
import com.legendshop.basic.entity.Protocol;
import com.legendshop.basic.query.ProtocolQuery;

/**
 * (Protocol)表数据库访问层
 *
 * @author legendshop
 */
public interface ProtocolDao extends GenericDao<Protocol, Long> {


	/**
	 * 查询协议分页列表
	 *
	 * @param protocolQuery
	 * @return
	 */
	PageSupport<Protocol> queryPageList(ProtocolQuery protocolQuery);

	/**
	 * 根据协议代号获取对应协议信息
	 *
	 * @param code
	 * @return
	 */
	Protocol getByCode(String code);
}
