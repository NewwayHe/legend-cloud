/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.activity.bo.FullActiveProductBO;
import com.legendshop.activity.dao.ShippingProductDao;
import com.legendshop.activity.entity.ShippingProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 包邮活动商品Dao
 *
 * @author legendshop
 */
@Repository
public class ShippingProductDaoImpl extends GenericDaoImpl<ShippingProduct, Long> implements ShippingProductDao {

	@Override
	public List<FullActiveProductBO> queryByActiveIdByShopId(Long activeId, Long shopId) {
		String sql = "SELECT p.id AS productId,p.name AS productName,p.pic,p.actual_stocks AS actualStocks,p.cash as price,p.rec_date " +
				"FROM ls_product p INNER JOIN (SELECT * FROM ls_shipping_product WHERE shipping_id =? AND shop_id = ?)o " +
				"ON p.id = o.product_id";
		return this.query(sql, FullActiveProductBO.class, activeId, shopId);
	}

	@Override
	public List<ShippingProduct> queryByShippingIdAndShopId(Long ShippingId, Long shopId) {
		return query("SELECT * FROM ls_shipping_product WHERE shipping_id = ? AND shop_id = ?", ShippingProduct.class, ShippingId, shopId);
	}

	@Override
	public void deleteByIdAndShopId(Long id, Long shopId) {
		update("DELETE FROM ls_shipping_product WHERE shipping_id = ? AND shop_id = ?", id, shopId);
	}

}
