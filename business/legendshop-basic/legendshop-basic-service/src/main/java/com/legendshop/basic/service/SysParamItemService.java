/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.query.SysParamItemQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;

import java.util.List;

/**
 * (SysParamItem)表服务接口
 *
 * @author legendshop
 * @since 2020-08-28 14:17:39
 */
public interface SysParamItemService extends BaseService<SysParamItemDTO> {


	/**
	 * 分页查询
	 *
	 * @param sysParamItemQuery
	 * @return
	 */
	PageSupport<SysParamItemDTO> queryPageList(SysParamItemQuery sysParamItemQuery);


	/**
	 * 根据主配置id获取配置项
	 *
	 * @param parentId
	 * @param config
	 * @param <T>
	 * @return
	 */
	<T> T getConfigDtoByParentId(Long parentId, Class<T> config);


	/**
	 * 获取配置项
	 *
	 * @param parentId
	 * @return
	 */
	List<SysParamItemDTO> getListByParentId(Long parentId);


	/**
	 * 修改配置项
	 *
	 * @param sysParamItemDTOS
	 * @return
	 */
	void updateSysParamItems(List<SysParamItemDTO> sysParamItemDTOS);


	/**
	 * 根据ID查找配置项
	 */
	R<SysParamItemDTO> getSysParamId(Long id);
}
