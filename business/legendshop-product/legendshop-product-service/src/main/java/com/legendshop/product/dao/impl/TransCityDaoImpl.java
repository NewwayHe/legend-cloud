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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.product.dao.TransCityDao;
import com.legendshop.product.entity.TransCity;
import com.legendshop.product.enums.TransCityTypeEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 每个城市的运费设置(TransCity)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2020-09-04 15:13:48
 */
@Repository
public class TransCityDaoImpl extends GenericDaoImpl<TransCity, Long> implements TransCityDao {

	@Override
	public void batchAdd(List<TransCity> transCities) {
		save(transCities);
	}

	@Override
	public void delByTransId(Long transId) {
		String sql = "DELETE FROM ls_trans_city WHERE trans_id = ?";
		update(sql, transId);
	}

	@Override
	public List<TransCity> getCityList(Long transId, Long parentId) {
		if (ObjectUtil.isEmpty(transId) || ObjectUtil.isEmpty(parentId)) {
			return null;
		}
		return queryByProperties(new EntityCriterion().eq("transId", transId).eq("parentId", parentId));
	}

	@Override
	public List<TransCity> getCityList(Long transId, List<Long> parentIds) {
		if (ObjectUtil.isEmpty(transId) || CollUtil.isEmpty(parentIds)) {
			return null;
		}
		return queryByProperties(new EntityCriterion().eq("transId", transId).in("parentId", parentIds));
	}

	@Override
	public List<TransCity> getCityList(List<Long> transIds, Long parentId) {
		if (CollUtil.isEmpty(transIds) || ObjectUtil.isEmpty(parentId)) {
			return null;
		}
		return queryByProperties(new EntityCriterion().in("transId", transIds).eq("parentId", parentId));
	}


	@Override
	public TransCity getByTransIdAndType(Long transId, Long cityId, TransCityTypeEnum transCityTypeEnum) {
		return getByProperties(new EntityCriterion().eq("transId", transId).eq("cityId", cityId).eq("type", transCityTypeEnum.value()));
	}

}
