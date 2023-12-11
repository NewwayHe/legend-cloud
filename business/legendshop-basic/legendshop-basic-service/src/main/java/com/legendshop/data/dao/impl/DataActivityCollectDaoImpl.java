/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao.impl;

import cn.legendshop.jpaplus.criterion.MatchMode;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.data.dao.DataActivityCollectDao;
import com.legendshop.data.dto.DataActivityCollectDTO;
import com.legendshop.data.entity.DataActivityCollect;
import com.legendshop.data.service.convert.DataActivityCollectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 营销活动汇总表(DataActivityCollect)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-06-30 20:35:13
 */
@Repository
public class DataActivityCollectDaoImpl extends GenericDaoImpl<DataActivityCollect, Long> implements DataActivityCollectDao {

	@Autowired
	private DataActivityCollectConverter dataActivityCollectConverter;

	@Override
	public List<DataActivityCollectDTO> getUnFinishedCollect(String orderTypeEnum) {
		List<DataActivityCollect> dataActivityCollects = queryByProperties(new LambdaEntityCriterion<>(DataActivityCollect.class).eq(DataActivityCollect::getStatus, 0).like(DataActivityCollect::getType, orderTypeEnum, MatchMode.ANYWHERE));
		return dataActivityCollectConverter.to(dataActivityCollects);
	}

	@Override
	public Integer deleteUnfinishedCollect() {
		String sql = "delete from ls_data_activity_collect where status = 0";
		return update(sql);
	}
}
