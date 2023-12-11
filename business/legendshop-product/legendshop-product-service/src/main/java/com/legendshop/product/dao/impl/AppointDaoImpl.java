/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.product.dao.AppointDao;
import com.legendshop.product.entity.Appoint;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品预约上架(Appoint)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-08-12 18:11:36
 */
@Repository
public class AppointDaoImpl extends GenericDaoImpl<Appoint, Long> implements AppointDao {

	@Override
	public Appoint queryByProductId(Long productId) {
		return get("SELECT * from ls_appoint a where a.on_sell_flag=0 and a.product_id=? ORDER BY create_time desc limit 0,1",
				Appoint.class, productId);
	}

	@Override
	public List<Appoint> queryByProductId(List<Long> appointIdList) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * from ls_appoint a where a.on_sell_flag=0 and a.product_id IN( ");
		for (Long id : appointIdList) {
			buffer.append("?,");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append(")");
		return query(buffer.toString(), Appoint.class, appointIdList.toArray());
	}
}
