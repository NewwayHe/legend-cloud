/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.user.dao.PassportItemDao;
import com.legendshop.user.entity.PassportItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class PassportItemDaoImpl extends GenericDaoImpl<PassportItem, Long> implements PassportItemDao {

	@Override
	public List<PassportItem> getByPassPortId(Long passPortId) {
		return super.queryByProperties(new EntityCriterion().eq("passPortId", passPortId));
	}

	@Override
	public PassportItem getByThirdPartyIdentifier(String thirdPartyIdentifier) {
		return super.getByProperties(new EntityCriterion().eq("thirdPartyIdentifier", thirdPartyIdentifier));
	}
}
