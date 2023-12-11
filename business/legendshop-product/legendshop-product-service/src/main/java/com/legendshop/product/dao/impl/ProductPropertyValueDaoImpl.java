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
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dao.ProductPropertyValueDao;
import com.legendshop.product.dto.ProductPropertyDTO;
import com.legendshop.product.entity.ProductPropertyValue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商品属性值服务
 *
 * @author legendshop
 */
@Repository
public class ProductPropertyValueDaoImpl extends GenericDaoImpl<ProductPropertyValue, Long> implements ProductPropertyValueDao {

	@Override
	public List<ProductPropertyValue> getByPropId(long propertyId) {
//		return this.query("SELECT v.* from ls_product_property p,ls_product_property_value v " +
//				"where p.id=v.prop_id and p.delete_flag=0 and ISNULL(v.shop_id) and  v.delete_flag=0 and p.id=? " +
//				"ORDER BY v.sequence",ProductPropertyValue.class,propertyId);
		return this.query("SELECT v.* from ls_product_property p,ls_product_property_value v " +
				"where p.id=v.prop_id and p.delete_flag=0 and  v.delete_flag=0 and p.id=? " +
				"ORDER BY v.sequence", ProductPropertyValue.class, propertyId);
	}

	@Override
	public ProductPropertyValue getByPropId(long propertyId, String name) {
		return get("select * from ls_product_property_value where delete_flag=0  and name=? and prop_id=?", ProductPropertyValue.class, name, propertyId);

	}


	@Override
	public void updateDeleteFlag(Long valueId, Boolean deleteFlag) {
		update("update ls_product_property_value set delete_flag= ? ,update_time= NOW() where id = ? ", deleteFlag, valueId);
	}

	@Override
	public void updateDeleteFlagByPropId(Long id, boolean deleteFlag) {
		update("update ls_product_property_value set delete_flag= ? ,update_time= NOW() where prop_id = ? ", deleteFlag, id);
	}

	@Override
	public List<ProductPropertyValue> queryByPropId(List<Long> productIdList) {
		List<Long> ids = new ArrayList<Long>();
		if (CollUtil.isEmpty(productIdList)) {
			return Collections.emptyList();
		}
		StringBuffer sb = new StringBuffer("select a.id,a.prop_id,a.name,a.sequence,a.pic from ls_product_property_value a where a.delete_flag=0");
		sb.append(" and ISNULL(a.shop_id) and a.prop_id in(");
		for (Long id : productIdList) {
			ids.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")ORDER BY a.prop_id,a.sequence");
		return query(sb.toString(), ProductPropertyValue.class, ids.toArray());
	}

	@Override
	public List<ProductPropertyBO> queryByGroupId(Long id) {
		return query("SELECT p.* from ls_product_property p,ls_product_property_param_group pg where  delete_flag=0 and " +
				"pg.prop_id=p.id and p.attribute_type ='P' and pg.group_id=? ORDER BY pg.seq", ProductPropertyBO.class, id);
	}

	@Override
	public List<ProductPropertyBO> queryByGroupId(List<Long> groupIds) {
		StringBuffer sql = new StringBuffer("select p.*,gr.group_id from ls_product_property_param_group gr ,ls_product_property p " +
				"where gr.prop_id=p.id and gr.group_id in ( ");
		Object[] params = new Object[groupIds.size()];
		for (int i = 0; i < groupIds.size(); i++) {
			params[i] = groupIds.get(i);
			sql.append("?,");
		}
		sql.setLength(sql.length() - 1);
		sql.append(" ) order by gr.seq");
		return this.query(sql.toString(), ProductPropertyBO.class, params);
	}

	@Override
	public List<ProductPropertyValue> getAllProductPropertyValue(List<ProductPropertyDTO> propertyList) {
		if (CollUtil.isEmpty(propertyList)) {
			return null;
		}
		Object[] propIds = new Object[propertyList.size()];
		for (int i = 0; i < propIds.length; i++) {
			propIds[i] = propertyList.get(i).getId();
		}
		return this.queryByProperties(new EntityCriterion().in("propId", propIds).addAscOrder("sequence"));
	}


	@Override
	public List<ProductPropertyValue> getProductPropertyValue(List<Long> ids) {
		return this.queryAllByIds(ids);
	}


	@Override
	public List<Long> getValueIdsByPropId(Long propId) {
		return query("SELECT id FROM ls_product_property_value WHERE prop_id=? and delete_flag=false", Long.class, propId);
	}

}
