/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.dto.ProductArrivalNoticeDTO;
import com.legendshop.product.entity.ProductArrivalNotice;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 到货通知转换器
 *
 * @author legendshop
 */
@Mapper
public interface ProductArrivalNoticeConverter extends BaseConverter<ProductArrivalNotice, ProductArrivalNoticeDTO> {

	/**
	 * to bo
	 *
	 * @param productArrivalNotice
	 * @return
	 */
	ProductArrivalNoticeBO convert2BO(ProductArrivalNotice productArrivalNotice);

	/**
	 * to bo List
	 *
	 * @param productArrivalNoticeList
	 * @return
	 */
	List<ProductArrivalNoticeBO> convert2BoList(List<ProductArrivalNotice> productArrivalNoticeList);

	/**
	 * to bo pageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ProductArrivalNoticeBO> convert2BoPageList(PageSupport<ProductArrivalNotice> ps);
}
