/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao.impl;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.ProtocolDao;
import com.legendshop.basic.entity.Protocol;
import com.legendshop.basic.query.ProtocolQuery;
import org.springframework.stereotype.Repository;

/**
 * (Protocol)表数据库访问层实现
 *
 * @author legendshop
 */
@Repository
public class ProtocolDaoImpl extends GenericDaoImpl<Protocol, Long> implements ProtocolDao {

	@Override
	public PageSupport<Protocol> queryPageList(ProtocolQuery protocolQuery) {
		CriteriaQuery cq = new CriteriaQuery(Protocol.class, protocolQuery.getPageSize(), protocolQuery.getCurPage());
		cq.like("name", protocolQuery.getName(), MatchMode.ANYWHERE);
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public Protocol getByCode(String code) {
		if (StrUtil.isBlank(code)) {
			return null;
		}
		return this.getByProperties(new EntityCriterion().eq("code", code));
	}
}
