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
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.entity.VitLog;
import com.legendshop.product.query.VitLogQuery;

import java.util.List;

/**
 * 访问历史Dao
 *
 * @author legendshop
 */
public interface VitLogDao extends GenericDao<VitLog, Long> {

	/**
	 * 用户端 商品浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	List<VitLog> vitLogListPage(VitLogQuery vitLogQuery);


	/**
	 * 查询用户浏览历史记录的分页信息。
	 *
	 * @param vitLogQuery 查询条件
	 * @return 包含用户浏览历史记录的分页信息
	 */
	PageSupport<VitLog> queryVitListPage(VitLogQuery vitLogQuery);

	/**
	 * 查询用户浏览历史记录的分页信息。
	 *
	 * @param pageSize  页面大小
	 * @param curPage   当前页码
	 * @return 包含用户浏览历史记录的分页信息
	 */
	PageSupport<VitLog> queryUserVitList(Integer pageSize, Integer curPage);

	/**
	 * 查询特定用户在24小时内是否有访问特定商品的记录。
	 *
	 * @param productId 商品ID
	 * @param userId    用户ID
	 * @return 包含特定用户在24小时内访问特定商品的记录
	 */
	VitLogDTO getByUserIdAndProductId(Long productId, Long userId);

	/**
	 * 计算特定用户的访问历史记录数量。
	 *
	 * @param userId 用户ID
	 * @return 特定用户的访问历史记录数量
	 */
	Integer userVisitLogCount(Long userId);


	/**
	 * 获取同一IP,30分钟后，的店铺首页浏览历史
	 *
	 * @param recordDTO
	 * @return
	 */
	VitLog getVisitedShopIndexLog(VitLogRecordDTO recordDTO);

	/**
	 * 获取同一IP,30分钟后的商品浏览历史
	 *
	 * @param vitLogRecordDTO
	 * @return
	 */
	VitLog getVisitedProductLog(VitLogRecordDTO vitLogRecordDTO);

	/**
	 * 用户端获取浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	PageSupport<VitLog> queryVitListPageByUser(VitLogQuery vitLogQuery);

	/**
	 * 删除特定用户对特定商品的浏览记录。
	 *
	 * @param userId    用户ID
	 * @param productId 商品ID
	 * @return 删除操作是否成功，成功返回 true，失败返回 false
	 */
	Boolean deleteUserVitLog(Long userId, Long productId);

	/**
	 * 查询用户浏览记录（最近20条）
	 *
	 * @param userId
	 * @param shopId
	 * @return
	 */
	List<VitLogDTO> queryVitList(Long userId, Long shopId);
}
