/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.criterion.Criterion;
import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.criterion.Restrictions;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.dao.TransportDao;
import com.legendshop.product.entity.Transport;
import com.legendshop.product.enums.TransCityQueryTypeEnum;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.enums.TransTypeEnum;
import com.legendshop.product.query.TransportQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class TransportDaoImpl extends GenericDaoImpl<Transport, Long> implements TransportDao {

	@Override
	public List<Transport> getTransports(Long shopId) {
		return query("select ltp.id as id,ltp.trans_name as transName from ls_transport ltp where ltp.status = 1 and ltp.shop_id = ?", Transport.class, shopId);
	}

	@Override
	public Long saveTransprot(Transport transport) {
		if (transport.getFreeFlag()) {
			transport.setConditionFreeFlag(false);
		}
		if (TransTypeEnum.CONSTANT.value().equals(transport.getTransType())) {
			transport.setConditionFreeFlag(false);
		}
		return save(transport);
	}

	@Override
	public int updateTransport(Transport transport) {
		if (transport.getFreeFlag()) {
			transport.setConditionFreeFlag(false);
		}
		if (TransTypeEnum.CONSTANT.value().equals(transport.getTransType())) {
			transport.setConditionFreeFlag(false);
		}
		return update(transport);
	}

	@Override
	public Long getByName(String name, Long id) {
		String sql = "SELECT id FROM ls_transport WHERE trans_name = ? and shop_id=? ";
		return get(sql, Long.class, name, id);

	}

	@Override
	public PageSupport<Transport> queryTransportPage(TransportQuery transportQuery) {
		CriteriaQuery cq = new CriteriaQuery(Transport.class, transportQuery, true);
		//cq.addOr()
		Criterion cqs = cq.or(Restrictions.eq("shopId", transportQuery.getShopId()), Restrictions.eq("shopId", "-1"));
		cq.add(cqs);
		cq.like("transName", transportQuery.getTransName(), MatchMode.ANYWHERE);
		TransCityQueryTypeEnum typeEnum = TransCityQueryTypeEnum.fromCode(transportQuery.getTransQueryType());
		switch (typeEnum) {
			case AREA_LIMIT:
				cq.eq("freeFlag", true);
				break;
			case FREIGHT_CAL:
				cq.notEq("transType", TransCityTypeEnum.CONSTANT_FREIGHT.value());
				cq.eq("freeFlag", false);
				break;
			case CONSTANT_FREIGHT:
				cq.eq("transType", TransCityTypeEnum.CONSTANT_FREIGHT.value());
				cq.eq("freeFlag", false);
				break;
			default:
		}
		cq.addAscOrder("shopId");
		cq.addDescOrder("recDate");
		return queryPage(cq);
	}
}
