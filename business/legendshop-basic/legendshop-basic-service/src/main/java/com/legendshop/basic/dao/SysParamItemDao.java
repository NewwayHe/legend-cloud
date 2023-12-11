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
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.basic.entity.SysParamItem;
import com.legendshop.basic.query.SysParamItemQuery;

import java.util.List;

/**
 * (SysParamItem)表数据库访问层
 *
 * @author legendshop
 * @since 2020-08-28 14:17:38
 */
public interface SysParamItemDao extends GenericDao<SysParamItem, Long> {


	/**
	 * 分页查询
	 *
	 * @param sysParamItemQuery
	 * @return
	 */
	PageSupport<SysParamItem> queryPageList(SysParamItemQuery sysParamItemQuery);


	/**
	 * 根据主配置id获取配置项
	 *
	 * @param parentId
	 * @return
	 */
	List<SysParamItem> getListByParentId(Long parentId);


	/**
	 * 修改配置项的value
	 *
	 * @param sysParamValueItemDTOS
	 * @return
	 */
	void updateValueOnlyById(List<SysParamValueItemDTO> sysParamValueItemDTOS);

	/**
	 * 是否启用
	 *
	 * @param parentId
	 * @return
	 */
	Boolean getEnabledByParentId(Long parentId);

	/**
	 * 根据parentIds获取所有是否启用
	 *
	 * @param parentIds
	 * @return
	 */
	List<SysParamItemDTO> getEnabledByParentIds(List<Long> parentIds);

	/**
	 * 批量添加
	 *
	 * @param itemDTOList
	 */
	void batchAddItems(List<SysParamItemDTO> itemDTOList);
}
