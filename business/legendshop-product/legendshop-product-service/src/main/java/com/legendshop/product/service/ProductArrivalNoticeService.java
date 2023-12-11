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
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductArrivalNoticeDTO;
import com.legendshop.product.query.ProductArrivalNoticeQuery;

import java.util.List;

/**
 * 商品到货通知服务
 *
 * @author legendshop
 */
public interface ProductArrivalNoticeService {

	/**
	 * 根据skuId获取已添加到货通知的分页列表
	 *
	 * @param productArrivalNoticeQuery
	 * @return
	 */
	PageSupport<ProductArrivalNoticeBO> getSelectArrival(ProductArrivalNoticeQuery productArrivalNoticeQuery);

	/**
	 * 到货通知商品查询的方法。
	 *
	 * @param query 到货通知商品查询对象
	 * @return 到货通知商品的分页支持对象
	 */
	PageSupport<SkuBO> productPage(ProductArrivalNoticeQuery query);


	/**
	 * 发送到货通知
	 *
	 * @param arrivalSkuIdList 到货的skuId
	 */
	void noticeUser(List<SkuBO> arrivalSkuIdList);

	/**
	 * 保存到货通知的方法。
	 *
	 * @param productArrivalNoticeDTO 到货通知信息
	 * @return 保存后的到货通知ID
	 */
	R<Long> saveProdArriInfo(ProductArrivalNoticeDTO productArrivalNoticeDTO);

}
