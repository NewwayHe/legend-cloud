/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransportDTO;
import com.legendshop.product.query.TransportQuery;

/**
 * 运费模板服务
 *
 * @author legendshop
 */
public interface TransportService {

	/**
	 * 根据id获取
	 *
	 * @param id
	 * @return
	 */
	TransportBO getById(Long id);

	Long getByName(String name, Long id);

	/**
	 * 查询带上模板里带的city
	 *
	 * @param id 运费模板ID
	 * @return
	 */
	TransportBO getTransport(Long id);

	/**
	 * 保存
	 *
	 * @param transportDTO
	 * @return
	 */
	Long saveTransport(TransportDTO transportDTO);

	// TransportBO getDetailedTransport(Long shopId,Long id);

	/**
	 * 删除模板
	 *
	 * @param id
	 * @return
	 */
	int deleteTransport(Long id);

	/**
	 * 更新模板
	 *
	 * @param transportDTO
	 * @return
	 */
	int updateTransport(TransportDTO transportDTO);


	/**
	 * 运费模板管理列表
	 *
	 * @param transportQuery
	 * @return
	 */
	PageSupport<TransportBO> queryTransportPage(TransportQuery transportQuery);

	/**
	 * 商品发布选择运费模板列表
	 *
	 * @param transportQuery
	 * @return
	 */
	PageSupport<TransportBO> queryTransportChoosePage(TransportQuery transportQuery);


}
