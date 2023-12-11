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
import com.legendshop.basic.dto.AdminProtocolDTO;
import com.legendshop.basic.dto.ProtocolDTO;
import com.legendshop.basic.query.ProtocolQuery;
import com.legendshop.common.core.constant.R;

/**
 * (Protocol)表服务接口
 *
 * @author legendshop
 */
public interface ProtocolService {


	/**
	 * 获取协议分页列表
	 *
	 * @param protocolQuery
	 * @return
	 */
	PageSupport<ProtocolDTO> queryPageList(ProtocolQuery protocolQuery);

	/**
	 * 更新协议链接,富文本
	 *
	 * @param adminProtocolDTO
	 * @return
	 */
	R<Void> updateUrl(AdminProtocolDTO adminProtocolDTO);

	/**
	 * 根据协议代号获取协议信息
	 *
	 * @param code
	 * @return
	 */
	R<ProtocolDTO> getByCode(String code);

	/**
	 * 查看协议
	 *
	 * @param id
	 * @return
	 */
	R<ProtocolDTO> getById(Long id);
}
