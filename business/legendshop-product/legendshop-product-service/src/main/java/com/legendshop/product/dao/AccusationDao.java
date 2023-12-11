/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.IllegalProductBO;
import com.legendshop.product.entity.Accusation;
import com.legendshop.product.excel.IllegalExportDTO;
import com.legendshop.product.query.AccusationQuery;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.query.ProductQuery;

import java.util.List;

/**
 * 举报表Dao.
 *
 * @author legendshop
 */
public interface AccusationDao extends GenericDao<Accusation, Long> {

	AccusationBO getAccusation(Long id);

	PageSupport<AccusationBO> queryAccusation(AccusationReportQuery accusationQuery);

	/**
	 * 查询我的举报，用户端
	 *
	 * @param accusationQuery
	 * @return
	 */
	PageSupport<AccusationBO> queryMyAccusation(AccusationQuery accusationQuery);

	/**
	 * 根据举报类型id查询举报记录数
	 *
	 * @param longList
	 * @return
	 */
	Integer getCountByTypeId(List<Long> longList);

	/**
	 * 商品举报列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<AccusationBO> shopPage(AccusationQuery query);

	/**
	 * 违规商品查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<IllegalProductBO> illegalPage(ProductQuery query);

	/**
	 * 违规商品导出
	 *
	 * @param query
	 * @return
	 */
	List<IllegalExportDTO> illegalList(ProductQuery query);

	/**
	 * 根据商品Id获取最新举报信息
	 *
	 * @param prodIds
	 * @return
	 */
	List<AccusationBO> queryLatestAccusation(List<Long> prodIds);
}
