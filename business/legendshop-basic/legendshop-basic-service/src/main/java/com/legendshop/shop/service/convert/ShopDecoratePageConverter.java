/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.bo.ShopDecoratePageBO;
import com.legendshop.shop.dto.ShopDecoratePageDTO;
import com.legendshop.shop.entity.ShopDecoratePage;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface ShopDecoratePageConverter extends BaseConverter<ShopDecoratePage, ShopDecoratePageDTO> {


	/**
	 * to boPageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<ShopDecoratePageBO> convert2BoPageList(PageSupport<ShopDecoratePage> ps);

	ShopDecoratePageBO convert2BoPageList(ShopDecoratePage ps);
}
