/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.enums.ReceiverEnum;
import com.legendshop.shop.query.PubQuery;

/**
 * 公告服务，分为买家公告和卖家公告.
 *
 * @author legendshop
 */
public interface PubService {

	/**
	 * 根据ID获取详情
	 *
	 * @param id
	 * @param userId
	 * @param receiverEnum
	 * @return
	 */
	R<PubDTO> getPubById(Long id, Long userId, ReceiverEnum receiverEnum);

	/**
	 * 根据ID删除
	 *
	 * @param id
	 * @return
	 */
	int deletePub(Long id);

	/**
	 * 保存
	 *
	 * @param pub
	 * @return
	 */
	Long save(PubDTO pub);

	/**
	 * 更新
	 *
	 * @param pub
	 * @return
	 */
	int update(PubDTO pub);


	/**
	 * 分页查询列表
	 *
	 * @param pubQuery
	 * @return
	 */
	PageSupport<PubDTO> getPubListPage(PubQuery pubQuery);

	/**
	 * 根据类型获取最新一条公告
	 *
	 * @return
	 */
	PubDTO getNewestPubByType(Integer type);

	/**
	 * 根据类型获取公告列表
	 *
	 * @return
	 */
	PageSupport<PubDTO> queryPubPageListByType(PubQuery pubQuery);

	/**
	 * 公告上线下线
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	R updateStatus(Long id, Integer status);

	R<Integer> userUnreadMsg(Long userId);
}
