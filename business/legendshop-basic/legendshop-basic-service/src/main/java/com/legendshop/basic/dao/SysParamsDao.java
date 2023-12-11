/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.entity.SysParams;
import com.legendshop.basic.query.SysParamPageQuery;
import com.legendshop.basic.query.SysParamQuery;

import java.util.List;

/**
 * (SysParams)表数据库访问层
 *
 * @author legendshop
 * @since 2020-08-28 12:00:45
 */
public interface SysParamsDao extends GenericDao<SysParams, Long> {


	/**
	 * 分页查询
	 *
	 * @param sysParamQuery
	 * @return
	 */
	PageSupport<SysParams> queryPageList(SysParamQuery sysParamQuery);

	/**
	 * 根据name获取
	 *
	 * @param name
	 * @return
	 */
	SysParams getByName(String name);

	/**
	 * 根据分组获取相关配置
	 *
	 * @param group
	 * @return
	 */
	List<SysParams> getByGroup(String group);

	PageSupport<SysParams> getByGroupPage(SysParamPageQuery query);
}
