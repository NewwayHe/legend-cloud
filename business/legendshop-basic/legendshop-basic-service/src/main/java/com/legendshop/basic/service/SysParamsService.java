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
import com.legendshop.basic.dto.BatchUpdateSysParamDTO;
import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.query.SysParamPageQuery;
import com.legendshop.basic.query.SysParamQuery;
import com.legendshop.common.core.service.BaseService;

import java.util.List;

/**
 * (SysParams)表服务接口
 *
 * @author legendshop
 * @since 2020-08-28 12:00:46
 */
public interface SysParamsService extends BaseService<SysParamsDTO> {


	/**
	 * 分页查询
	 *
	 * @param sysParamQuery
	 * @return
	 */
	PageSupport<SysParamsDTO> queryPageList(SysParamQuery sysParamQuery);

	/**
	 * 根据name获取
	 *
	 * @param name
	 * @return
	 */
	SysParamsDTO getByName(String name);


	/**
	 * 根据name列表获取
	 *
	 * @param sysParamNameEnums
	 * @return
	 */
	List<SysParamsDTO> getByNames(List<SysParamNameEnum> sysParamNameEnums);

	/**
	 * 根据主配置的name得到配置的dto
	 *
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T getConfigDtoByParamName(String name, Class<T> clazz);

	<T> T getNotCacheConfigByName(String name, Class<T> clazz);


	/**
	 * 根据名称获取
	 *
	 * @param name
	 * @return
	 */
	List<SysParamItemDTO> getSysParamItemsByParamName(String name);


	/**
	 * 获取已启用的支付方式
	 *
	 * @return
	 */
	List<PayTypeDTO> getEnabledPayType();

	List<PayTypeDTO> getUseEnabledPayType();

	List<SysParamsDTO> getByGroup(SysParamGroupEnum group);


	/**
	 * 保存推送配置及自动生成子项
	 *
	 * @param sysParamsDTO
	 * @return
	 */
	Long savePushSys(SysParamsDTO sysParamsDTO);

	PageSupport<SysParamsDTO> getByGroupPage(SysParamPageQuery query);


	/**
	 * 缓存清理
	 */
	void cleanCache();

	/**
	 * 修改配置项的value
	 */
	void updateValueOnlyById(BatchUpdateSysParamDTO batchUpdateSysParamDTO);
}
