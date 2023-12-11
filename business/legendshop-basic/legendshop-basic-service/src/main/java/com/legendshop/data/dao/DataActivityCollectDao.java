/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.data.dto.DataActivityCollectDTO;
import com.legendshop.data.entity.DataActivityCollect;

import java.util.List;

/**
 * 营销活动汇总表(DataActivityCollect)表数据库访问层
 *
 * @author legendshop
 * @since 2021-06-30 20:35:12
 */
public interface DataActivityCollectDao extends GenericDao<DataActivityCollect, Long> {

	/**
	 * 查询未完成的汇总
	 *
	 * @return
	 */
	List<DataActivityCollectDTO> getUnFinishedCollect(String orderTypeEnum);

	/**
	 * 删除未完成的汇总
	 */
	Integer deleteUnfinishedCollect();

}
