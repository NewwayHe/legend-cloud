/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.user.entity.PassportItem;

import java.util.List;

/**
 * @author legendshop
 */
public interface PassportItemDao extends GenericDao<PassportItem, Long> {

	List<PassportItem> getByPassPortId(Long passPortId);

	PassportItem getByThirdPartyIdentifier(String thirdPartyIdentifier);
}
