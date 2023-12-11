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
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.entity.ProductArrivalNotice;
import com.legendshop.product.query.ProductArrivalNoticeQuery;

import java.util.List;

/**
 * 到货通知
 *
 * @author legendshop
 */
public interface ProductArrivalNoticeDao extends GenericDao<ProductArrivalNotice, Long> {

	/**
	 * 根据userId，skuId，status获得用户已经添加的到货通知
	 *
	 * @param userId 用户ID
	 * @param skuId  SkuID
	 * @param status 通知状态
	 * @return
	 */
	ProductArrivalNotice getAlreadySaveUser(Long userId, Long skuId, Integer status);

	/**
	 * 根据skuId获取已添加到货通知的分页列表
	 *
	 * @param productArrivalNoticeQuery
	 * @return
	 */
	PageSupport<ProductArrivalNoticeBO> getSelectArrival(ProductArrivalNoticeQuery productArrivalNoticeQuery);

	/**
	 * 到货通知商品查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<SkuBO> productPage(ProductArrivalNoticeQuery query);

	/**
	 * 根据skuId查询到货通知
	 *
	 * @param arricalSkuIdList
	 * @return
	 */
	List<ProductArrivalNotice> queryBySkuId(List<Long> arricalSkuIdList);

	/**
	 * 根据需要更新的ID列表批量更新状态的方法。
	 *
	 * @param needUpdateIds 需要更新状态的ID列表
	 * @param status        更新后的状态值
	 * @return 更新操作影响的行数
	 */
	int updateStatusByIds(List<Long> needUpdateIds, Integer status);
}
