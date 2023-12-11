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
import com.legendshop.shop.bo.DecoratePageBO;
import com.legendshop.shop.dto.DecoratePageDTO;
import com.legendshop.shop.entity.DecoratePage;
import org.mapstruct.Mapper;

/**
 * @author legendshop
 */
@Mapper
public interface DecoratePageConverter extends BaseConverter<DecoratePage, DecoratePageDTO> {

	/**
	 * to boPageList
	 *
	 * @param ps
	 * @return
	 */
	PageSupport<DecoratePageBO> convert2BoPageList(PageSupport<DecoratePage> ps);
}
