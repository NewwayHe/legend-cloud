/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.entity.Transport;
import com.legendshop.product.query.TransportQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface TransportDao extends GenericDao<Transport, Long> {

	List<Transport> getTransports(Long shopId);

	PageSupport<Transport> queryTransportPage(TransportQuery transportQuery);

	Long saveTransprot(Transport transport);

	int updateTransport(Transport transport);

	Long getByName(String name, Long id);
}
