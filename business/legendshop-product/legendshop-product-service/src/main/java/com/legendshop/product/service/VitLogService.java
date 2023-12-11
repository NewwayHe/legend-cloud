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
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dto.CartVitLogDTO;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.dto.VitLogUserHistoryDTO;
import com.legendshop.product.query.VitLogQuery;

import java.util.List;

/**
 * 浏览历史服务类.
 *
 * @author legendshop
 */
public interface VitLogService {


	/**
	 * 根据用户获取浏览历史的统计数量
	 *
	 * @param userId
	 * @return
	 */
	R<Integer> userVisitLogCount(Long userId);


	/**
	 * 用户端 个人中心，浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	PageSupport<VitLogDTO> queryVitListPage(VitLogQuery vitLogQuery);


	/**
	 * 异步记录浏览历史
	 *
	 * @param vitLogRecordDTO
	 */
	void recordVitLog(VitLogRecordDTO vitLogRecordDTO);

	/**
	 * 用户获取浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	PageSupport<VitLogDTO> queryVitListPageByUser(VitLogQuery vitLogQuery);


	/**
	 * 用户获取浏览历史
	 *
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	PageSupport<VitLogUserHistoryDTO> queryUserVitList(Integer pageSize, Integer curPage);

	/**
	 * 把用户浏览记录作删除标记
	 *
	 * @param userId
	 * @param productId
	 * @return
	 */
	Boolean deleteVitLog(Long userId, Long productId);

	/**
	 * 异步保存浏览记录
	 *
	 * @param bo      产品对象
	 * @param userId  用户ID
	 * @param source  来源信息
	 */
	void saveProdView(ProductBO bo, Long userId, String source);

	/**
	 * 异步保存浏览记录
	 *
	 * @param shopId
	 * @param userId
	 * @param source
	 */
	void saveShopView(Long shopId, Long userId, String source);

	/**
	 * 异步保存浏览记录
	 *
	 * @param cartVitLogDTO
	 */
	void saveProdCartView(CartVitLogDTO cartVitLogDTO);


	/**
	 * 批量发送活动访问记录MQ
	 *
	 * @param cartVitLogList
	 */
	void batchSaveProdCartView(List<CartVitLogDTO> cartVitLogList);

	/**
	 * 查询用户浏览记录（最近20条）
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	R<List<VitLogDTO>> queryVitList(Long userId, Long shopId);
}
