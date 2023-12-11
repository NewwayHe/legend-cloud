/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.CriteriaQuery;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.product.bo.ParamGroupBO;
import com.legendshop.product.bo.ProductPropertyBO;
import com.legendshop.product.dao.ParamGroupDao;
import com.legendshop.product.entity.ParamGroup;
import com.legendshop.product.enums.ProductPropertySourceEnum;
import com.legendshop.product.query.ParamGroupQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 参数组Dao.
 *
 * @author legendshop
 */
@Repository
public class ParamGroupDaoImpl extends GenericDaoImpl<ParamGroup, Long> implements ParamGroupDao {

	@Override
	public PageSupport<ParamGroup> getParamGroupPage(ParamGroupQuery paramGroupQuery) {
		CriteriaQuery cq = new CriteriaQuery(ParamGroup.class, paramGroupQuery.getPageSize(), paramGroupQuery.getCurPage());
		cq.like("name", paramGroupQuery.getName(), MatchMode.ANYWHERE);
		cq.eq("source", paramGroupQuery.getSource());
		cq.eq("shopId", paramGroupQuery.getShopId());
		cq.addDescOrder("createTime");
		return queryPage(cq);
	}

	@Override
	public List<Long> getPropIdByGroupId(Long id) {
		return query("select p.id from ls_product_property p,ls_product_property_param_group g where g.prop_id=p.id and group_id=? ", Long.class, id);
	}

	@Override
	public List<ParamGroupBO> queryAllOnline() {
		return queryDTOByProperties(new LambdaEntityCriterion<>(ParamGroupBO.class).eq(ParamGroupBO::getSource, ProductPropertySourceEnum.SYSTEM.getValue()));
	}

	@Override
	public List<ProductPropertyBO> getPropByGroupId(Long id) {
		return query("select p.id,p.prop_name from ls_product_property p,ls_product_property_param_group g where g.prop_id=p.id and group_id=? ", ProductPropertyBO.class, id);
	}

}
