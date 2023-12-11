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
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.dao.ProductGroupRelationDao;
import com.legendshop.product.entity.ProductGroupRelation;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dao实现类
 *
 * @author legendshop
 */
@Repository
public class ProductGroupRelationDaoImpl extends GenericDaoImpl<ProductGroupRelation, Long> implements ProductGroupRelationDao {


	@Override
	public int deleteProdGroupRelevanceByGroupId(Long groupId) {
		String sql = "delete from ls_product_group_relation where group_id=?";
		return update(sql, groupId);
	}

	@Override
	public int getProductByGroupId(Long productId, Long groupId) {
		return get("SELECT COUNT(1) FROM ls_product_group_relation WHERE product_id=? and group_id=?", Integer.class, productId, groupId);
	}

	@Override
	public List<Long> getProductIdListByGroupId(Long prodGroupId) {
		String sql = "SELECT product_id FROM ls_product_group_relation,ls_product s WHERE s.id=product_id AND s.status=? AND s.op_status=? AND s.del_status=? AND group_id=?";
		return query(sql, Long.class, ProductStatusEnum.PROD_ONLINE.value(), OpStatusEnum.PASS.getValue(), ProductDelStatusEnum.PROD_NORMAL.getValue(), prodGroupId);
	}

}
