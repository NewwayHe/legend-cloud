/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dao.ProductPropertyParamGroupDao;
import com.legendshop.product.entity.ProductPropertyParamGroup;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @author legendshop
 */
@Repository
public class ProductPropertyParamGroupDaoImpl extends GenericDaoImpl<ProductPropertyParamGroup, Long> implements ProductPropertyParamGroupDao {

	@Override
	public int deleteByGroupId(Long id) {
		return update("delete from ls_product_property_param_group g where g.group_id=?", id);
	}

	@Override
	public int getCountByGroupId(Long id) {
		return get("select count(1) from ls_product_property_param_group where group_id=? ", Integer.class, id);
	}

	@Override
	public List<ProductPropertyBO> queryPropertyNameByGroupId(List<Long> groupIdList) {
		if (CollUtil.isEmpty(groupIdList)) {
			return Collections.emptyList();
		}

		StringBuffer sb = new StringBuffer("select b.id,group_id,b.prop_name from ls_product_property_param_group " +
				"a ,ls_product_property b where a.prop_id=b.id and b.delete_flag=0 and  a.group_id in( ");
		for (Long id : groupIdList) {
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY group_id,seq");
		return query(sb.toString(), ProductPropertyBO.class, groupIdList.toArray());
	}
}
